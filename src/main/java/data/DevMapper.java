package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author caspe
 */
public class DevMapper {
    
    public boolean loadZipcodesFromFile(File file) {
        boolean status = false;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            Connection con = Connector.connection();
            
            String str;
            while ((str = br.readLine()) != null) {
                String parts[] = str.split(" ", 2);
                int zip =  Integer.parseInt(parts[0]);
                String city = parts[1];
                
                System.out.println("zip: " + zip + " city: " + city);
                
                String query = "REPLACE INTO zipcodes (zip, city) VALUES (?, ?)";
                PreparedStatement ps = con.prepareStatement(query);
                
                ps.setInt(1, zip);
                ps.setString(2, city);
                
                ps.executeUpdate();
                
                status = true;
            }
            
            
            
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        return status;
        
    }
    
    public static void main(String[] args) {
        String path = "C:\\Users\\caspe\\Desktop\\zipcodes.txt";
        String _path = ".\\zipcodes.txt";
        File file = new File(_path);
        System.out.println(new File(".").getAbsolutePath());
        
        System.out.println(new DevMapper().loadZipcodesFromFile(file));
        
    }
    
}
