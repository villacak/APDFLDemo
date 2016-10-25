package com.proquest.demo.utils;


import com.proquest.demo.constants.Constants;
import com.proquest.demo.enums.HttpCode;
import com.proquest.demo.enums.MessagesKeys;
import com.proquest.demo.enums.PropertyFileNames;
import com.proquest.demo.pojos.GenericData;
import com.proquest.demo.properties.PropertyReaderLib;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by kvillaca on 7/15/16.
 */
public class FileUtils {

    private static final String COMPONENT_NAME = FileUtils.class.getName();

    private PropertyReaderLib pr;

    public FileUtils() {
        pr = new PropertyReaderLib(PropertyFileNames.MESSAGES);
    }

    /**
     * Get the path for the temporary folder
     *
     * @param fileName
     * @return
     */
    public Path getTempPdfPath(final String fileName) {
        String tempPdfsVariableValue = System.getenv(Constants.ENVIRONMENT_VAR_FOR_TEMPPDFS);
        if (tempPdfsVariableValue == null || tempPdfsVariableValue.length() == 0) {
            tempPdfsVariableValue = Constants.DEFAULT_PATH;
        }
        final StringBuilder sbPath = new StringBuilder(tempPdfsVariableValue)
                .append(Constants.SLASH_FORWARD);

        boolean isSuccess = false;
        final File folder = new File(sbPath.toString());
        final String osName = System.getProperty("os.name");
        if (!folder.exists()) {
            if (!osName.contains("windows") && !osName.contains("Windows") && !osName.contains("mac") && !osName.contains("Mac")) {
                Process p = null;
                Process p1 = null;
                try {
                    p = Runtime.getRuntime().exec(new String[]{"bash", "-c", "sudo mkdir " + sbPath.toString()});
                    p1 = Runtime.getRuntime().exec(new String[]{"bash", "-c", "sudo chmod 777 " + sbPath.toString()});
                    if (p != null && p1 != null) {
                        p.waitFor();
                        p1.waitFor();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                p = null;
                p1 = null;
            }
        }

        Path filePath = null;
        final File fileWithName = new File(folder, fileName);
        filePath = fileWithName.toPath();
        final StringBuilder sbMsg = new StringBuilder(pr.getPropertyValue(MessagesKeys.READ_FILE_WITH_SUCCESS.toString()))
                .append(Constants.BLANK_SPACE).append(fileName);

        tempPdfsVariableValue = null;
        return filePath;
    }


    /**
     * Read file from file system
     *
     * @param path
     * @return
     */
    public byte[] readFileFromTempLocation(final Path path) {
        final String READ = "r";

        byte[] fileAsBytesToReturn = null;

        final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        try {
            lock.readLock().lock();
            final RandomAccessFile readFile = new RandomAccessFile(path.toString(), READ);
            final FileChannel inChannel = readFile.getChannel();

            final int unchangedPdfLength = (int) readFile.length();
            final ByteBuffer buffer = ByteBuffer.allocate(unchangedPdfLength);
            final int numberOfBytesRead = inChannel.read(buffer);
            inChannel.force(true);
            buffer.flip();
            fileAsBytesToReturn = buffer.array();

            inChannel.close();
            readFile.close();
        } catch (Exception e) {
            fileAsBytesToReturn = null;
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
        return fileAsBytesToReturn;
    }


    /**
     * Write file to the file system
     *
     * @param path
     * @param fileAsBytes
     * @return
     */
    public HttpCode writeFileToTempLocation(final Path path, final byte[] fileAsBytes) {
        final String WRITE = "rw";

        HttpCode codeToReturn = HttpCode.SUCCESS;
        final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        try {
            lock.writeLock().lock();
            final RandomAccessFile writeFile = new RandomAccessFile(path.toString(), WRITE);
            final FileChannel outChannel = writeFile.getChannel();
            final ByteBuffer buffer = ByteBuffer.wrap(fileAsBytes, 0, fileAsBytes.length);
            buffer.flip();

            writeFile.write(buffer.array());
            outChannel.force(true);

            outChannel.close();
            writeFile.close();
        } catch (Exception e) {
            codeToReturn = HttpCode.ERROR;
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
        return codeToReturn;
    }


    /**
     * Delete file from file system
     *
     * @param path
     * @return
     */
    public HttpCode deleteFile(final Path path) {
        HttpCode codeToReturn = HttpCode.SUCCESS;
        if (path == null) {
            final StringBuilder sb = new StringBuilder("deleteFile").append(": ").append(path);
        } else {
            try {
                boolean isDeleted = Files.deleteIfExists(path);
//                if (isDeleted) {
//                    System.out.println(pr.getPropertyValue(MessagesKeys.DELETE_FILE_WITH_SUCCESS.toString()));
//                } else {
//                    System.out.println(pr.getPropertyValue(MessagesKeys.FILE_NOT_FOUND.toString()));
//                }
            } catch (IOException e) {
               e.printStackTrace();
            }
        }
        return codeToReturn;
    }


    /**
     * Return the local path to read and write locally those pdfs
     *
     * @param pathFile
     * @return
     */
    public Path getLocalPathForReadOrWriteFrom(final String pathFile) {
        final File file = new File(pathFile);
        final String fileName = file.getName();
        final String tempFileName = getTemporaryFileName(fileName);
        final Path path = getTempPdfPath(tempFileName);
        return path;
    }


    /**
     * Return the local path to read and write locally those pdfs
     *
     * @param pathFile
     * @return
     */
    public Path getImageLocalPathForReadOrWriteFrom(final String pathFile) {
        final String METHOD_NAME = "getLocalPathForReadOrWriteFrom";
        final File file = new File(pathFile);
        final String fileName = file.getName();
        final Path path = getTempPdfPath(fileName);
        return path;
    }



    /**
     * From the original file name, return the temporary file name
     * The temporary name is composed by the name + random UUID + .pdf
     *
     * @param originalFileName
     * @return
     */
    public String getTemporaryFileName(final String originalFileName) {
        final String TEMP = "temp";
        final int FILENAME = 0;

        final StringBuilder nameToReturn;
        final String[] splitFileName = originalFileName.split(Constants.PDF_SPLIT);
        if (splitFileName != null) {
            final String uuid = UUID.randomUUID().toString();
            nameToReturn = new StringBuilder(splitFileName[FILENAME]).append(uuid)
                    .append(Constants.PERIOD).append(TEMP);
        } else {
            nameToReturn = new StringBuilder(splitFileName[FILENAME]).append(Constants.PERIOD)
                    .append(TEMP);
        }
        return nameToReturn.toString();
    }


    /**
     * Return the path with filename from the resource folder
     *
     * @param classObj
     * @param s3url
     * @return
     * @throws IOException
     */
    public File getResourceFilePath(final Class classObj, final String s3url) throws IOException {
        final File file = new File(s3url);
        final String fileName = file.getName();
        URL resourceUrl = classObj.getClassLoader().getResource(fileName);
        if (resourceUrl == null) {
            final File tempFile = new File(classObj.getClassLoader().getResource(Constants.EMPTY).getFile() + fileName);
            final FileWriter writer = new FileWriter(tempFile);
            resourceUrl = classObj.getClassLoader().getResource(fileName);
        }
        final String uuid = UUID.randomUUID().toString();
        final StringBuilder nameWithTimeStampMills = new StringBuilder(resourceUrl.getFile()).append(Constants.UNDERSCORE).append(uuid);
        final File tempFile = new File(resourceUrl.getFile());
        resourceUrl = null;
        return tempFile;
    }


    /**
     * Return the path with file name from the local folder as String
     * (still need to look for the local folder)
     *
     * @param classObj
     * @param s3url
     * @return
     * @throws IOException
     */
    public String getResourceFilePathAsString(final Class classObj, final String s3url) throws IOException {
        final File tempFile = getResourceFilePath(classObj, s3url);
        return tempFile.toString();
    }


    /**
     * Clear all files from the temporary folder, this methos is used just when the service is started
     * if the folder doesn't exist, the method TRY to create the directory, though it's best it the
     * instance already have the directory created, via script or user.
     */
    public void clearFilesFromTempPdfsFolder() {
        String tempPdfsVariableValue = System.getenv(Constants.ENVIRONMENT_VAR_FOR_TEMPPDFS);
        if (tempPdfsVariableValue == null || tempPdfsVariableValue.length() == 0) {
            tempPdfsVariableValue = Constants.DEFAULT_PATH;
        }

        final File folder = new File(tempPdfsVariableValue);
        if (folder.isDirectory()) {
            final File[] filesInTempFolder = folder.listFiles();

            for (File file : filesInTempFolder) {
                deleteFile(file.toPath());
            }
        } else {
            folder.mkdirs();
        }
    }


    /**
     * Set the local path for read and write
     *
     * @param data
     * @return
     */
    public GenericData setLocalReadAndWritePath(final GenericData data) {
        final Path localPathRead = data.getInputFileS3() != null ? getLocalPathForReadOrWriteFrom(data.getInputFileS3()) : null;
        final Path localPathWrite = data.getOutputFileS3() != null ? getLocalPathForReadOrWriteFrom(data.getOutputFileS3()) : null;

        if (localPathRead != null)
            data.setInputFileLocal(localPathRead.toString());

        if (localPathWrite != null)
            data.setOutputFileLocal(localPathWrite.toString());
        return data;
    }

    /**
     * Set the local path for read and write
     *
     * @param data
     * @return
     */
    public GenericData setLocalImagesReadAndWritePath(final GenericData data) {
        final Path localPathRead = data.getInputFileS3() != null ? getImageLocalPathForReadOrWriteFrom(data.getInputFileS3()) : null;
        final Path localPathWrite = data.getOutputFileS3() != null ? getImageLocalPathForReadOrWriteFrom(data.getOutputFileS3()) : null;

        if (localPathRead != null)
            data.setInputFileLocal(localPathRead.toString());

        if (localPathWrite != null)
            data.setOutputFileLocal(localPathWrite.toString());
        return data;
    }
}
