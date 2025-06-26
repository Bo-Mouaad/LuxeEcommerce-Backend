package MainApp.Services;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor

public class CloudinaryService {
	final Cloudinary cloudinary;
	
	public String uploadImageHandlerWithFile(MultipartFile file) throws IOException {
	Map<?,?> res = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
	return (String) res.get("secure_url");
	}
	public String uploadImageHandlerWithURL(String URL) throws IOException {
		Map<?,?> res = cloudinary.uploader().upload(URL, ObjectUtils.emptyMap());
		return (String) res.get("secure_url");
		}
}
