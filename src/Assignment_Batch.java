/**
 * Created by shaunmarkham on 25/10/2017.
 */
import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

class Assignment_Batch {

    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable>  {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString().toLowerCase().replace(":","").replace(".","").replace("\"","").replace("\"\"","")
                    .replace("?","").replace("'","").replace(",","");
            StringTokenizer tokenizer = new StringTokenizer(line);
            while (tokenizer.hasMoreTokens()) {
                word.set(tokenizer.nextToken());
                context.write(word, one);
            }
        }
        //first map - map words from the same university that have a score appended to them defined by the
        //position of the university in the ranking (1-15)
    }

    private static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            context.write(key, new IntWritable(sum));
        }
        //reduce elements from initial map class named MAP
    }


    public static class Map2 extends Mapper<LongWritable, Text, Text, IntWritable>  {
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] line = value.toString().toLowerCase().split("[_|\t}]");
            line[0].replace(")","").replace("(","").replace("{","").replace("}","");

            if (line.length < 3 || line[0] == "") {
                return;
            }else {
                try {
                    int exa = Integer.parseInt(line[1]) + Integer.parseInt(line[2]) + 1;
                }
                catch (NumberFormatException ex) {
                    return;
                }

                IntWritable added_val = new IntWritable(Integer.parseInt(line[1]) + Integer.parseInt(line[2]));
                Text newword = new Text(line[0]);
            StringTokenizer tokken = new StringTokenizer(line[0]);
                try {
                    newword.set(tokken.nextToken());
                }
                catch (NoSuchElementException ey) {
                    return;
                }
                context.write(newword, added_val);
            }
        }
        //2nd map class, remove score from each individual word and add that to the occurrences to create
        //an artificial score based on the number of occurrences of a word and the origin of the word (which
        // University is using it)
    }


    private static class Reduce2 extends Reducer<Text, IntWritable, Text, DoubleWritable> {
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;

            for (IntWritable val : values) {
                sum += val.get();
            }
            context.write(key, new DoubleWritable(sum));
        }
        //final reduce, aggregate scores of the same word and that word to create the artificial score
    }

    public static void main(String[] args) throws Exception {

        ArrayList<University_Info> Uni_Info = Parse_Perform_Ops.Get_Uni_Info(); //Get info from REF xlsx file creating
        // University_Info objects in the process and saving them to an ArrayList

        ArrayList<Tuple> Info;
        Info = Parse_Perform_Ops.Parse_Read();
        //Read info from all .csv files present in data->Subject_directory directory

        ArrayList<University_Info> Total_Dept_Score = Parse_Perform_Ops.Tuple_Uni_Linker(Uni_Info, Info);
        //Add papers to their respective Universities based on their University code (ID) and Assessment name (to
        //differentiate between faculties/departments)

        java.util.Map<String, List<University_Info>> MultiMap;
        MultiMap = Parse_Perform_Ops.ReturnMultiple(Total_Dept_Score,1);
        //Separate Universities by assessment criteria, while applying a score to each University based on
        //paper ratings, sorting and keeping only top 15 for each criterion

        Parse_Perform_Ops.getBestandWorst(Total_Dept_Score);
        //method used to split papers into 2 separate files, one containing the top 15 university papers
        //and the other containing the rest, to be used in further work (applying a classifier, these
        // can be used as training data)
        java.util.Map<String, List<University_Info>> Appended = Parse_Perform_Ops.Append_Words(MultiMap);
        //append a score to each word based off of University position

        boolean preproc = false;
        for (String item : Appended.keySet()) {
            for (University_Info object : Appended.get(item)) {
                if (object.Contains_Tuples() == true) { //if University object contains papers
                    File file = new File(System.getProperty("user.dir") + "/input/" + "Input_Data" + ".txt");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    Parse_Perform_Ops.PrinterMethod(object, file);
                    //Print text to be used as input for mapreduce (in input folder)
                    preproc = true;
                } else {
                    continue;
                }
            }

            if (preproc == true) {
                //mapreduce portion
                String OUTPUT_PATH = "intermediate_output";

                Configuration conf1 = new Configuration();

                //FIRST JOB START

                @SuppressWarnings("deprecation") Job job = new Job(conf1, "REFAnalysis");

                job.setOutputKeyClass(Text.class);
                job.setOutputValueClass(IntWritable.class);
                job.setMapperClass(Map.class);
                job.setReducerClass(Reduce.class);

                job.setOutputKeyClass(Text.class);
                job.setOutputValueClass(IntWritable.class);

                job.setInputFormatClass(TextInputFormat.class);
                job.setOutputFormatClass(TextOutputFormat.class);

                FileInputFormat.addInputPath(job, new Path(args[0]));
                FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH));

                job.waitForCompletion(true);

                //FIRST JOB END
                //SECOND JOB START

                Job job2 = new Job(conf1, "Job 2");

                job2.setMapperClass(Map2.class);
                job2.setReducerClass(Reduce2.class);

                job2.setOutputKeyClass(Text.class);
                job2.setOutputValueClass(IntWritable.class);

                job2.setInputFormatClass(TextInputFormat.class);
                job2.setOutputFormatClass(TextOutputFormat.class);

                TextInputFormat.addInputPath(job2, new Path(OUTPUT_PATH));
                TextOutputFormat.setOutputPath(job2, new Path(args[1]));

                job2.waitForCompletion(true);
                //SECOND JOB END

                Parse_Perform_Ops.SortOutput();
                //get output and sort by value (score)
                Parse_Perform_Ops.Cleanup(item.substring(0, 3));
                //remove all redundant folders and files for mapreduce to be able to function on next iteration

                File file = new File(System.getProperty("user.dir") + "/input/" + "Input_Data" + ".txt");
                file.delete();
                File folder = new File(System.getProperty("user.dir") + "/intermediate_output/");
                FileUtils.deleteDirectory(folder);
                //remove redundant folder intermediate_output used to transfer data between mapreduce jobs

                preproc = false;
            }
        }
    }
}
