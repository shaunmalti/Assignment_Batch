/**
 * Created by shaunmarkham on 25/10/2017.
 */
import java.io.File;
import java.io.IOException;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;



class Assignment_Batch {

    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable>  {//mapper class has 4 values to
        //    specify the: input key, input value, output key and output value
//        mapper writes output using an instance of context class which is used to communicate
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString().toLowerCase().replace(":"," ").replace("."," ").replace("\""," ").replace("\"\""," ")
                    .replace("?"," ").replace("-"," ").replace("'"," ");
            StringTokenizer tokenizer = new StringTokenizer(line);
            while (tokenizer.hasMoreTokens()) {
                word.set(tokenizer.nextToken());
                context.write(word, one);
            }
        }
    }

    private static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
        //3 primary phases: shuffle, sort and reduce
        //parameters for reducer define the types of input and output key/value pairs
//                collector writes output to filesystem
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            context.write(key, new IntWritable(sum));
        }
    }
//the driver class contains the main

    public static void main(String[] args) throws Exception {


        ArrayList<University_Info> Uni_Info = Parse_Perform_Ops.Get_Uni_Info();


        ArrayList<Tuple> Info;
        Info = Parse_Perform_Ops.Parse_Read();

        //Parse_Perform_Ops.PrinterMethod(Info); //print titles to text file

        ArrayList<University_Info> Total_Dept_Score = Parse_Perform_Ops.Tuple_Uni_Linker(Uni_Info, Info); //this can be reduced on but how?

        //this is here for nothing, first i need to create subarraylists
        Collections.sort(Total_Dept_Score);

        java.util.Map<String, List<University_Info>> MultiMap;
        MultiMap = Parse_Perform_Ops.ReturnMultiple(Total_Dept_Score); //Splits Data depending on Assess name, return multimap with top 10 in each category


        //next step is go through values in MultiMap.getkeyset() in a for string vals:
        //check if the university contains tuples. if yes write to separate text file.
        boolean preproc = false;
        for (String item : MultiMap.keySet()) {
            for (University_Info object : MultiMap.get(item)) {
                if (object.Contains_Tuples() == true) {
                    File file = new File(System.getProperty("user.dir") + "/input/" + "Input_Data" + ".txt");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    Parse_Perform_Ops.PrinterMethod(object, file);
                    preproc = true;
                } else {
                    continue;
                }
            }

            if (preproc == true) {
                Configuration conf = new Configuration();

                @SuppressWarnings("deprecation") Job job = new Job(conf, "REFAnalysis");

                job.setOutputKeyClass(Text.class);
                job.setOutputValueClass(IntWritable.class);
                job.setMapperClass(Map.class);
                job.setReducerClass(Reduce.class);

                job.setOutputKeyClass(Text.class);
                job.setOutputValueClass(IntWritable.class);

                job.setInputFormatClass(TextInputFormat.class);
                job.setOutputFormatClass(TextOutputFormat.class);

                FileInputFormat.addInputPath(job, new Path(args[0]));
                FileOutputFormat.setOutputPath(job, new Path(args[1]));

                job.waitForCompletion(true);

                //this method moves result from output to outputlegacy and renames with object name
                //it also removes textfile in input
                Parse_Perform_Ops.SortOutput();
                Parse_Perform_Ops.Cleanup(item.substring(0, 3));
                preproc = false;
            }
        }
    }
}
