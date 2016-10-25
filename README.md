# APDFLDemo
A demo project using APDFL 15.0.0

For extract text URI and output file name path, you can add the string (_) to be replaced by an UUID string, r.g. text(_).txt will be
replaced by something similar to this text01d23a4f-ae49-442f-8cdf-b77d7a92d507.txt and the same is valid for the pdf output file.

The application will need two system variables defined, the TEMP_PDFS and APDFL_PATH e.g.:

Linux: 
export TEMP_PDFS = <path>/temppdfs 
export APDFL_PATH = <pathToTheAPDFL15.0.0>/Java/com.datalogics.PDFL.jar

MacOS: 
launchctl setenv TEMP_PDFS = <path>/temppdfs 
launchctl setenv APDFL_PATH = <pathToTheAPDFL15.0.0>/Java/com.datalogics.PDFL.jar

Windows:
set TEMP_PDFS = <path>/temppdfs
set APDFL_PATH = <pathToTheAPDFL15.0.0>/Java/com.datalogics.PDFL.jar

Note that the /temppdfs folder need to have read and write permission (good to use chmod 777 $TEMP_PDFS).

The code has reasonable amount of comments to help the understand.
Also the code has been made to emulate what JMeter would be doing.
For a quick run a good number to start is numberMaxOfRuns to 4 and maxThreads to 10.

String example:
private static final String jsonExample2 = "[{\"actionlist\":[\n" +
"{\"pdfAction\":\"linearize\",\"valueForActionList\":[{\"key\":\"linearize\",\"value\":\"true\"}]}\n" +
",{\"pdfAction\":\"addcopyrightpage\",\"valueForActionList\":[{\"key\":\"default\",\"value\":\"yes\"}]}\n" +
",{\"pdfAction\":\"removespecifiedpages\",\"valueForActionList\":[{\"key\":\"pages\",\"value\":\"1-3\"}]}\n" +
",{\"pdfAction\":\"extracttext\",\"valueForActionList\":[{\"key\":\"s3uri\",\"value\":\"**(The output path for the text file)**\"}]}\n" +
",{\"pdfAction\":\"addcopyrightnotice\",\"valueForActionList\":[{\"key\":\"text\",\"value\":\"test\"}]}\n" +
"]\n" +
",\"outputfile\":\"**(The output path for the PDF)**\"\n" +
",\"objectslist\":[\n" +
"{\"inputfile\":\"**(The input path, where is located the PDF to be manipulated)**\"\n" +
",\"mimetype\":\"application/pdf\"\n" +
",\"md5\":\"743de1e1e192a9cb6989c805727be776\"\n" +
",\"size\":\"511694\"}\n" +
"]\n" +
"}]";

For any other questions plese feel free to send me an email.
