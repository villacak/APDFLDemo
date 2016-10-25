package com.proquest.demo;

/**
 * Created by kvillaca on 10/21/16.
 */
public class MainClass {

//     MD5 and size are irrelevant now.
//     the (_) it's to be replaced by UUID
    private static final String jsonExample1 = "[{\"actionlist\":[\n" +
            "{\"pdfAction\":\"linearize\",\"valueForActionList\":[{\"key\":\"linearize\",\"value\":\"true\"}]}\n" +
            ",{\"pdfAction\":\"addcopyrightpage\",\"valueForActionList\":[{\"key\":\"default\",\"value\":\"yes\"}]}\n" +
            ",{\"pdfAction\":\"removespecifiedpages\",\"valueForActionList\":[{\"key\":\"pages\",\"value\":\"1-3\"}]}\n" +
            ",{\"pdfAction\":\"extracttext\",\"valueForActionList\":[{\"key\":\"s3uri\",\"value\":\"/Users/kvillaca/temp/86(_)Text.txt\"}]}\n" + // The text file output path
            ",{\"pdfAction\":\"addcopyrightnotice\",\"valueForActionList\":[{\"key\":\"text\",\"value\":\"test\"}]}\n" +
            "]\n" +
            ",\"outputfile\":\"/Users/kvillaca/temp/86Edited(_).pdf\"\n" + // The output path for the pdf, the (_) it's to be replaced by UUID
            ",\"objectslist\":[\n" +
            "{\"inputfile\":\"/Users/kvillaca/Desktop/HMS/pdfs/86.pdf\"\n" +  // The input path, where it will load the pdf
            ",\"mimetype\":\"application/pdf\"\n" +  // Don't need to worry about it
            ",\"md5\":\"743de1e1e192a9cb6989c805727be776\"\n" + // Don't need to worry about it
            ",\"size\":\"511694\"}\n" +
            "]\n" +
            "}]";

    //
    // Examples, please review all of them and add the PDF you want with the respective path,
    // as you can see in the next String you also can add extra items to the list, or use with one only.
    //
    private static final String jsonExample2 = "[{\"actionlist\":[\n" +
            "{\"pdfAction\":\"linearize\",\"valueForActionList\":[{\"key\":\"linearize\",\"value\":\"true\"}]}\n" +
            ",{\"pdfAction\":\"addcopyrightpage\",\"valueForActionList\":[{\"key\":\"default\",\"value\":\"yes\"}]}\n" +
            ",{\"pdfAction\":\"removespecifiedpages\",\"valueForActionList\":[{\"key\":\"pages\",\"value\":\"1-3\"}]}\n" +
            ",{\"pdfAction\":\"extracttext\",\"valueForActionList\":[{\"key\":\"s3uri\",\"value\":\"<The output path for the text file>\"}]}\n" +
            ",{\"pdfAction\":\"addcopyrightnotice\",\"valueForActionList\":[{\"key\":\"text\",\"value\":\"test\"}]}\n" +
            "]\n" +
            ",\"outputfile\":\"<The output path for the PDF>\"\n" +
            ",\"objectslist\":[\n" +
            "{\"inputfile\":\"<The input path, where is located the PDF to be manipulated>\"\n" +
            ",\"mimetype\":\"application/pdf\"\n" +
            ",\"md5\":\"743de1e1e192a9cb6989c805727be776\"\n" +
            ",\"size\":\"511694\"}\n" +
            "]\n" +
            "}," +
            "{\"actionlist\":[\n" +
            "\t\t\t\t{\"pdfAction\":\"linearize\",\"valueForActionList\":[{\"key\":\"linearize\",\"value\":\"true\"}]}\n" +
            "\t\t\t\t,{\"pdfAction\":\"addcopyrightpage\",\"valueForActionList\":[{\"key\":\"default\",\"value\":\"yes\"}]}\n" +
            "\t\t\t\t,{\"pdfAction\":\"removespecifiedpages\",\"valueForActionList\":[{\"key\":\"pages\",\"value\":\"1-3\"}]}\n" +
            "\t\t\t\t,{\"pdfAction\":\"extracttext\",\"valueForActionList\":[{\"key\":\"s3uri\",\"value\":\"<The output path for the text file>\"}]}\n" +
            "\t\t\t\t,{\"pdfAction\":\"addcopyrightnotice\",\"valueForActionList\":[{\"key\":\"text\",\"value\":\"test\"}]}\n" +
            "\t\t\t]\n" +
            ",\"outputfile\":\"<The output path for the PDF>\"\n" +
            ",\"objectslist\":[\n" +
            "\t\t\t\t{\"inputfile\":\"<The input path, where is located the PDF to be manipulated>\"\n" +
            "\t\t\t\t,\"mimetype\":\"application/pdf\"\n" +
            "\t\t\t\t,\"md5\":\"743de1e1e192a9cb6989c805727be776\"\n" +
            "\t\t\t\t,\"size\":\"511694\"}\n" +
            "\t]\n" +
            "}]";


    private static final String jsonExample3 = "[{\"actionlist\":[\n" +
            "{\"pdfAction\":\"linearize\",\"valueForActionList\":[{\"key\":\"linearize\",\"value\":\"true\"}]}\n" +
            ",{\"pdfAction\":\"addcopyrightpage\",\"valueForActionList\":[{\"key\":\"default\",\"value\":\"yes\"}]}\n" +
            ",{\"pdfAction\":\"removespecifiedpages\",\"valueForActionList\":[{\"key\":\"pages\",\"value\":\"1-3\"}]}\n" +
            ",{\"pdfAction\":\"extracttext\",\"valueForActionList\":[{\"key\":\"s3uri\",\"value\":\"http://hms-pdf-service-pre.s3.amazonaws.com/pre/in/pdfProcess.txt\"}]}\n" +
            ",{\"pdfAction\":\"addcopyrightnotice\",\"valueForActionList\":[{\"key\":\"text\",\"value\":\"test\"}]}\n" +
            "]\n" +
            ",\"outputfile\":\"https://s3.amazonaws.com/hms-pdf-service-pre/pre/in/pdfProcess.pdf\"\n" +
            ",\"objectslist\":[\n" +
            "{\"inputfile\":\"https://s3.amazonaws.com/hms-pdf-service-pre/pre/out/pdfDemo.pdf\"\n" +
            ",\"mimetype\":\"application/pdf\"\n" +
            ",\"md5\":\"743de1e1e192a9cb6989c805727be776\"\n" +
            ",\"size\":\"511694\"}\n" +
            "]\n" +
            "}]";

    private static final String jsonExample4 = "[{\"actionlist\":[\n" +
            "{\"pdfAction\":\"linearize\",\"valueForActionList\":[{\"key\":\"linearize\",\"value\":\"true\"}]}\n" +
            ",{\"pdfAction\":\"addcopyrightpage\",\"valueForActionList\":[{\"key\":\"default\",\"value\":\"yes\"}]}\n" +
            ",{\"pdfAction\":\"removespecifiedpages\",\"valueForActionList\":[{\"key\":\"pages\",\"value\":\"1-3\"}]}\n" +
            ",{\"pdfAction\":\"extracttext\",\"valueForActionList\":[{\"key\":\"s3uri\",\"value\":\"http://hms-pdf-service-pre.s3.amazonaws.com/pre/in/pdfProcess.txt\"}]}\n" +
            ",{\"pdfAction\":\"addcopyrightnotice\",\"valueForActionList\":[{\"key\":\"text\",\"value\":\"test\"}]}\n" +
            "]\n" +
            ",\"outputfile\":\"https://s3.amazonaws.com/hms-pdf-service-pre/pre/in/pdfProcess.pdf\"\n" +
            ",\"objectslist\":[\n" +
            "{\"inputfile\":\"https://s3.amazonaws.com/hms-pdf-service-pre/pre/out/pdfDemo.pdf\"\n" +
            ",\"mimetype\":\"application/pdf\"\n" +
            ",\"md5\":\"743de1e1e192a9cb6989c805727be776\"\n" +
            ",\"size\":\"511694\"}\n" +
            "]\n" +
            "}]";

    private static final String jsonExample5 = "[{\"actionlist\":[\n" +
            "{\"pdfAction\":\"linearize\",\"valueForActionList\":[{\"key\":\"linearize\",\"value\":\"true\"}]}\n" +
            ",{\"pdfAction\":\"addcopyrightpage\",\"valueForActionList\":[{\"key\":\"default\",\"value\":\"yes\"}]}\n" +
            ",{\"pdfAction\":\"removespecifiedpages\",\"valueForActionList\":[{\"key\":\"pages\",\"value\":\"1-3\"}]}\n" +
            ",{\"pdfAction\":\"extracttext\",\"valueForActionList\":[{\"key\":\"s3uri\",\"value\":\"http://hms-pdf-service-pre.s3.amazonaws.com/pre/in/pdfProcess.txt\"}]}\n" +
            ",{\"pdfAction\":\"addcopyrightnotice\",\"valueForActionList\":[{\"key\":\"text\",\"value\":\"test\"}]}\n" +
            "]\n" +
            ",\"outputfile\":\"https://s3.amazonaws.com/hms-pdf-service-pre/pre/in/pdfProcess.pdf\"\n" +
            ",\"objectslist\":[\n" +
            "{\"inputfile\":\"https://s3.amazonaws.com/hms-pdf-service-pre/pre/out/pdfDemo.pdf\"\n" +
            ",\"mimetype\":\"application/pdf\"\n" +
            ",\"md5\":\"743de1e1e192a9cb6989c805727be776\"\n" +
            ",\"size\":\"511694\"}\n" +
            "]\n" +
            "}]";

    private static final String jsonExample6 = "[{\"actionlist\":[\n" +
            "{\"pdfAction\":\"linearize\",\"valueForActionList\":[{\"key\":\"linearize\",\"value\":\"true\"}]}\n" +
            ",{\"pdfAction\":\"addcopyrightpage\",\"valueForActionList\":[{\"key\":\"default\",\"value\":\"yes\"}]}\n" +
            ",{\"pdfAction\":\"removespecifiedpages\",\"valueForActionList\":[{\"key\":\"pages\",\"value\":\"1-3\"}]}\n" +
            ",{\"pdfAction\":\"extracttext\",\"valueForActionList\":[{\"key\":\"s3uri\",\"value\":\"http://hms-pdf-service-pre.s3.amazonaws.com/pre/in/pdfProcess.txt\"}]}\n" +
            ",{\"pdfAction\":\"addcopyrightnotice\",\"valueForActionList\":[{\"key\":\"text\",\"value\":\"test\"}]}\n" +
            "]\n" +
            ",\"outputfile\":\"https://s3.amazonaws.com/hms-pdf-service-pre/pre/in/pdfProcess.pdf\"\n" +
            ",\"objectslist\":[\n" +
            "{\"inputfile\":\"https://s3.amazonaws.com/hms-pdf-service-pre/pre/out/pdfDemo.pdf\"\n" +
            ",\"mimetype\":\"application/pdf\"\n" +
            ",\"md5\":\"743de1e1e192a9cb6989c805727be776\"\n" +
            ",\"size\":\"511694\"}\n" +
            "]\n" +
            "}]";


    public static void main(String[] args) {
        // Set the JVM property
        // It's necessary have the APDFL_PATH=<yourAPDFLInstallPath>/Java/com.datalogics.PDFL.jar
        // I have defined it in a environmnet variable though you can insert the path as String into the setProperty
        // Second parameter
        final String APDFL_PATH = System.getenv("APDFL_PATH");
        System.setProperty("com.datalogics.PDFL.library.path", APDFL_PATH);

        final int numberMaxOfRuns = 4;//100; // This value is the equivalent to emulate the number
        final int maxThreads = 10; // This value is the equivalent to emulate the JMeter threads

        // Add more JSONs if necessary, ideally 10 is a good number.
        // Please use all 6, adding different files to each one, also please try use a larger file,
        // one would be enough (larger means 70mb up!).
        final String[] jsons = {jsonExample1}; //, jsonExample2, jsonExample3, jsonExample4, jsonExample5, jsonExample6};
        try {
            for (int runs = 0; runs < numberMaxOfRuns; runs++) {
                for (int i = 0; i < maxThreads; i++) {
                    final MyThreads threadTemp = new MyThreads(jsons);
                    threadTemp.start();
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }


    }


}
