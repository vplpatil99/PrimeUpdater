package Prime.ProgramUpdater;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
 
/**
 * A utility that downloads a file from a URL.
 * @author www.codejava.net
 *
 */
public class Main {
	 
	 private static final int BUFFER_SIZE = 4096;
 
    /**
     * Downloads a file from a URL
     * @param fileURL HTTP URL of the file to be downloaded
     * @param saveDir path of the directory to save the file
     * @throws IOException
     */
    public static void main(String args[]) throws IOException
    {
    	
    	/*Document doc = Jsoup.connect("http://primeud.000webhostapp.com/MyFiles/").get();
        for (Element file : doc.select("td.right td a")) {
            System.out.println(file.attr("href"));
        }*/
    	String Software=null,Version=null,city_name=null,downloadpath=null;
    	try {
    	ConnectionDB conobj=new ConnectionDB();
        Connection connection = conobj.getConnectionTallyserverDB();
        city_name=conobj.getCityName();
        Statement m_Statement = connection.createStatement();
        String query = "select software,version,ApplicationPath from tbl_SW_Update where updateavailabeYN='Y'";

        ResultSet m_ResultSet = m_Statement.executeQuery(query);
        if ((m_ResultSet.next())) {
        	Software=m_ResultSet.getString(1);
        	Version=m_ResultSet.getString(2);
        	downloadpath=m_ResultSet.getString(3);
        }
        m_Statement.close();
        m_ResultSet.close();
        query="select software,version from tbl_update_log where software='"+Software+"' and version='"+Version+"' and lab='"+city_name+"'";
        m_Statement = connection.createStatement();
        m_ResultSet = m_Statement.executeQuery(query);
        if ((m_ResultSet.next())) {
        	System.exit(0);
        }
        
        Iterator itr=getfilelist("http://primeud.000webhostapp.com/MyFiles/").iterator();
        
        while(itr.hasNext()){
        String filename=itr.next().toString();
    	downloadFile("http://primeud.000webhostapp.com/MyFiles/"+filename,downloadpath);
    	unzip(downloadpath+filename, downloadpath);
    	File file = new File(downloadpath+filename); 
        if(file.delete()) 
        { 
            System.out.println("File deleted successfully"); 
        } 
        else
        { 
            System.out.println("Failed to delete the file"); 
        } 
        }
        
    	Statement st = connection.createStatement(); 
        st.executeUpdate("INSERT INTO tbl_update_log(software,version,lab,date) VALUES ('"+Software+"','"+Version+"','"+city_name+"',getdate())");
    	}catch(Exception e) {
    		System.out.print(e);
    	}

    	
    }
    
   
    public static void downloadFile(String fileURL, String saveDir)
            throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
 
        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();
 
            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }
 
            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);
 
            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + File.separator + fileName;
             
            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
 
            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
 
            outputStream.close();
            inputStream.close();
 
            System.out.println("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    } 
	public static ArrayList<String> getfilelist(String webaddress) {
		ArrayList<String> list=new ArrayList<String>();
		URL url;

		try {
			// get URL content
			url = new URL(webaddress);
			URLConnection conn = url.openConnection();

			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));

			String inputLine;


			while ((inputLine = br.readLine()) != null) {
				String line=inputLine;
				if(line.indexOf("<a href")>-1)
				{
					String temp=line.substring(line.indexOf("<a href"));

						String temp1=temp.substring(9,temp.indexOf(">")-1);
						if(temp1.indexOf(".")>-1)
						{
							list.add(temp1);
						}
				}
			}

			br.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to "+newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}