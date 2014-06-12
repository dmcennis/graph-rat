/**
 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)
 */
package nz.ac.waikato.mcennis.rat.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;

import nz.ac.waikato.mcennis.rat.crawler.Crawler;
import nz.ac.waikato.mcennis.rat.crawler.WebCrawler;

/**
 * Class for writing the contents of a data stream to a file.  Designed to be 
 * called from multiple instances and writing to the same directory without corruption.
 * @author Daniel McEnnis
 *
 */
public class ToFileParser implements Parser {

    static private java.io.File repository;
    static Integer number = new Integer(0);
    String id = "ToFileParser";
    String subDirectory  = "";
    String filename = null;

    /**
         * Create a FileParser saving to the temporary directory.
         */
    public ToFileParser() {
        try {
            repository = java.io.File.createTempFile("None", "Void");
            repository = repository.getParentFile();
        } catch (IOException e) {
            repository = null;
        }
    }

    /**
         * Create a FileParser saving to the given location.
         * @param location directory the files are saved in.
         */
    public ToFileParser(String location) {
        repository = new java.io.File(location);
    }

    /**
         * Save to the next available file number
         * @param data data to be saved
         */
    public void parse(InputStream data) {
        if ((repository != null) && (repository.exists())) {
            File dest = null;
            if(filename == null){
                synchronized (number) {
                    dest = new File(repository.getAbsolutePath() + File.separator + subDirectory + number.toString());
                    number = new Integer(number.intValue() + 1);
                }
            }else{
                File dir = new File(repository.getAbsolutePath() + File.separator + subDirectory);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                dest = new File(repository.getAbsolutePath() + File.separator + subDirectory +filename);
            }
            if (dest != null) {
                try {
                    java.io.BufferedOutputStream output = new java.io.BufferedOutputStream(new java.io.FileOutputStream(dest));
                    byte[] buffer = new byte[1024];
                    int num_read = 0;
                    while ((num_read = data.read(buffer)) > 0) {
                        output.write(buffer, 0, num_read);
                    }
                    output.flush();
                    output.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
         * Same as parse
         * @param data stream to be saved to a file
         * @param crawler ignored
         */
    public void parse(InputStream data, Crawler crawler) {
        parse(data);
    }

    @Override
	public Parser duplicate() {
        ToFileParser ret = new ToFileParser(repository.getAbsolutePath());
        ret.subDirectory = subDirectory;
        ret.filename = filename;
        return ret;
    }

    /**
         * Set the count to a pre-set value - used if continuing a previous run
         * @param i filenumber to be set to
         */
    public void setCount(Integer i) {
        number = i;
    }

    /**
         * Intentionally null
         * @return null
         */
    public ParsedObject get() {
        return null;
    }

    /**
         * Intentionally a no-op
         * @param o ignored
         */
    public void set(ParsedObject o) {
        ; //intentionally null
    }

    public void setName(String name) {
        id = name;
    }

    public String getName() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSubDirectory() {
        return subDirectory;
    }

    public void setSubDirectory(String subDirectory) {
        this.subDirectory = subDirectory;
    }
    
    
}
