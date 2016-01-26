package br.com.extremeantcheatclan.dropbox;

import java.io.InputStream;
import java.util.Locale;

import br.com.extremeantcheatclan.view.LoginHelper;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.its.easyjavadropbox.api.impl.EasyJavaDropBoxServiceImpl;

public class DropBoxApiDonwload {
	
	private static final String TOKEM_PERMANENTE_DROPBOX = 
			"enNlHFeilhUAAAAAAAAP2dpfT_2TstOYOQVmOVzSZ1iirjS01fTcyxYY1myL51mI";
	
	public static byte[] getDonwloadExtremeAntCheat(String aqruivo){
		DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",
				Locale.getDefault().toString());
		try {
			DbxClient client = new DbxClient(config, TOKEM_PERMANENTE_DROPBOX);

			DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");

			for (DbxEntry child : listing.children) {
				if(child.name.equals(aqruivo)){
					String parseSize = child.toString().split("numBytes")[1].split(",")[0].replace("=", "");
					
					EasyJavaDropBoxServiceImpl upload = new EasyJavaDropBoxServiceImpl(TOKEM_PERMANENTE_DROPBOX);
					InputStream body = upload.getClient().startGetFile(child.path, "").body;
					
					long size = Long.valueOf(parseSize);
					byte[] arquivo = LoginHelper.inputStreamToByte(body, size);
					
					return arquivo;
				}
			}
		} catch (DbxException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
