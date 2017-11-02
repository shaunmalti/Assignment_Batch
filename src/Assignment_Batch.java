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
            //Change this to lowercase all characters --- also replacing many other characters
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

    private static class Reduce2 extends Reducer<Text, IntWritable, Text, IntWritable> {
        //3 primary phases: shuffle, sort and reduce
        //parameters for reducer define the types of input and output key/value pairs
//                collector writes output to filesystem
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            //at this point change input values

            //do reduces in here depending on scores


            for (IntWritable val : values) {
                sum += val.get();
            }
            context.write(key, new IntWritable(sum));
        }

    }

    public static void main(String[] args) throws Exception {


        ArrayList<University_Info> Uni_Info = Parser_Class_With_Tags.Get_Uni_Info();


        ArrayList<Tuple> Info;
        Info = Parser_Class_With_Tags.Parse_Read();

        Parser_Class_With_Tags.PrinterMethod(Info); //print titles to text file

        ArrayList<University_Info> Total_Dept_Score = Parser_Class_With_Tags.Tuple_Uni_Linker(Uni_Info, Info); //this can be reduced on but how?

        File folder = new File(System.getProperty("user.dir") + "/data/Subject_data");
        File[] listOfFiles = folder.listFiles();
        Collections.sort(Total_Dept_Score);

        //TODO AT this point I have a list of uni_info objects that have lists of tuples within them
        //TODO These objects are sorted in the Total_Dept_score arraylist of University_info objects

        Configuration conf = new Configuration();

        @SuppressWarnings("deprecation") Job job = new Job(conf, "REFAnalysis");


        //make first mapreduce here -- that is
        //again sort according to assessment names and then
        //sort again according to scores
        //after that perform mapreduce on all titles within those (for each separate csv file, where)
        //where getUni_Tuples within University_Info object is not empty

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);


        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setReducerClass(Reduce2.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);



        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
    }
    //the driver class contains the main






}
