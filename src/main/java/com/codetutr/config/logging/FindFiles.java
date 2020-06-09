package com.codetutr.config.logging;

import java.io.File;
import java.io.FileFilter;
import java.util.StringTokenizer;
import java.util.Vector;

public class FindFiles implements FileFilter
{

  public static final String DEFAULT_DIR_SEPERATOR = ";";
  private String directoryPath = "";
  private String seperator = DEFAULT_DIR_SEPERATOR;
  private String filename = "";
  private String beginPart = null;
  private String endPart = null;
  private int direction = 1;
  private File[] files = null;
  private Vector directories = null;
  private int nextFileIndex = 0;
  private int nextDirIndex = 0;

  
  public FindFiles()
  {
    super();
  }
  
  
  
  public FindFiles(String newFilename, String newDirectoryPath)
  {
    super();
    this.filename = newFilename;
    this.directoryPath = newDirectoryPath;
    
    int index = filename.indexOf('*');
    if (index >= 0)
    {
      this.beginPart = filename.substring(0, index);
      this.endPart   = filename.substring(index + 1);
    }
    else
    {
      this.beginPart = null;
    }
  }

  
  
  
  
  public File nextFile()
  {
    while ((this.files == null) || (this.nextFileIndex >= this.files.length))
    {
      if (this.directories == null)
      {
    	  if ((this.directoryPath == null) || (this.seperator == null)) 
    	  {
    		  this.directories  = new Vector(); 
    		  this.nextDirIndex = 0;
    	  }
    	  else
    	  {
    		  StringTokenizer tokens = new StringTokenizer(directoryPath, seperator); 
    		  this.directories  = new Vector(); 
    		  
    		  for (int i = 0; tokens.hasMoreTokens(); i++)
    		  {
    			  this.directories.add(new File(tokens.nextToken()));  
    		  }
    		  this.nextDirIndex = (direction == 1) ? 0 : directories.size()- 1;
    	  }
      }
      
      
      File nextDir = null;
      if ((0 <= this.nextDirIndex) && (this.nextDirIndex < this.directories.size()))
      {
    	  nextDir    = (File)this.directories.get(this.nextDirIndex);
          this.nextDirIndex += this.direction; 
      }
      
      
      if (nextDir == null)
      {
        return null;
      }
      

      this.files         = nextDir.listFiles(this);
      this.nextFileIndex = 0;
      
    } // While loop ends
    
    
    /* If No files returned, process next directory. We are Using Recursion here...*/
    if (this.files==null)
    {
		this.nextFileIndex++;
    	return nextFile();
    }
    
    
    
    /* If Directory returned, add to directory list. We are Using Recursion here... */
	if (this.files[this.nextFileIndex].isDirectory())
	{
		if (this.direction == 1)
		{
			this.directories.add(this.files[this.nextFileIndex]);
		} else 
		{
			this.directories.add(0, this.files[this.nextFileIndex]);
			this.nextDirIndex++;			
		}
		this.nextFileIndex++;
		return nextFile();		
	}
	
    return this.files[this.nextFileIndex++];
  }
 
  
  
  
  
  

  public boolean accept(File aFile)
  {

    boolean rc = false;

    if (aFile.isDirectory()){
    	rc=true;
    } else if (aFile.isFile()){
      String name = aFile.getName();

      if (beginPart == null)
      {
        rc = name.equals(filename);
      }
      else if (name.startsWith(beginPart))
      {
        rc = name.endsWith(endPart);
      }
    }

    return rc;
  }

  /**
   * Parses directory path based on seperator building an array of File objects.
   * Note: no check is actually do to see if path exists and is directory.
   * 
   * @return java.lang.String
   */
  protected Vector getDirectories()
  {

    if (directories == null)
    {
      if ((directoryPath == null) || (seperator == null))
      {
        directories  = new Vector();
        nextDirIndex = 0;
      }
      else
      {
        StringTokenizer tokens = new StringTokenizer(directoryPath,
                                   seperator);

        directories = new Vector();

        for (int i = 0; tokens.hasMoreTokens(); i++)
        {
          directories.add(new File(tokens.nextToken()));
        }

        nextDirIndex = (direction == 1)
                       ? 0
                       : directories.size()- 1;
      }
    }

    return directories;
  }

  /**
   * Insert the method's description here.
   * 
   * @return java.lang.String
   */
  public java.lang.String getDirectoryPath()
  {
    return directoryPath;
  }

  /**
   * Insert the method's description here.
   * 
   * @return java.lang.String
   */
  public java.lang.String getFilename()
  {
    return filename;
  }

  /**
   * Insert the method's description here.
   * 
   * @return java.lang.String
   */
  public java.lang.String getSeperator()
  {
    return seperator;
  }

  /**
   * Insert the method's description here.
   * 
   * @return boolean
   */
  public boolean isReverseSearch()
  {
    return direction == -1;
  }

  /**
   * Moves Find file object to next directory in path.
   * NOTE: no check is done if path is actually an directory.
   * 
   * @return java.lang.String
   */
 



  /**
   * Reset findFiles to start search over.
   * 
   */
  public void reset()
  {
    directories = null;
    files       = null;
  }

  /**
   * Insert the method's description here.
   * 
   * @param newDirectoryPath java.lang.String
   */
  public void setDirectoryPath(java.lang.String newDirectoryPath)
  {

    directoryPath = (newDirectoryPath == null)
                    ? ""
                    : newDirectoryPath;
  }



  /**
   * Sets direction that nextDirectory on true is last to firsyt,
   * (default) false is last to first.
   * 
   * @param newReverseSearch boolean
   */
  public void setReverseSearch(boolean newReverseSearch)
  {

    direction = newReverseSearch
                ? -1
                : 1;

    reset();
  }

  /**
   * Insert the method's description here.
   * 
   * @param newSeperator java.lang.String
   */
  public void setSeperator(java.lang.String newSeperator)
  {
    seperator = newSeperator;
  }
}


