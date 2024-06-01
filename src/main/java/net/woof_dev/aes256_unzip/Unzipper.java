package net.woof_dev.aes256_unzip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.apache.tika.Tika;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.woof_dev.aes256_unzip.dto.ExtractFileValue;
import net.woof_dev.aes256_unzip.dto.ExtractListValue;
import net.woof_dev.aes256_unzip.dto.ZipStatusValue;
import net.woof_dev.aes256_unzip.util.JsonUtils;

public class Unzipper {
	/**
	 * 解凍処理を実施します。
	 * @param source
	 * @param password
	 * @return
	 * @throws IOException 
	 */
	public String[] extract(String source, String password) throws IOException {
		Path destination = null;
		try {
			destination = this.getUniqueDirPath("save_");
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
		System.out.println(destination.toAbsolutePath());

		ZipFile zipFile = new ZipFile(source);
		try {
			if (zipFile.isEncrypted()) {
				zipFile.setPassword(password.toCharArray());
			}
			zipFile.extractAll(destination.toAbsolutePath().toString()); // すべてを解凍
		} catch (ZipException e) {
			System.out.println(e.getLocalizedMessage());
			throw e;
		}

		return getFileArray(destination);
	}	
	
	
	/**
	 * ZIPファイルが正しいか、パスワードが必要かチェックします。
	 * @param base64Content
	 * @return
	 */
	public String checkZipFile(String base64Content) {
		ZipStatusValue value = new ZipStatusValue();
		byte[] byteArray = Base64.getDecoder().decode(base64Content);
		String loadFile = this.saveTargetFile(byteArray);
		value.virtualFilePath = loadFile;
		ZipFile zipFile = new ZipFile(loadFile);
		if (!zipFile.isValidZipFile()) {
			value.errorMsg = "Invalid Zip File.";
			return JsonUtils.toJson(value);
		}
		try {
			if (zipFile.isEncrypted()) {
				value.isEncrypted = true;
				return JsonUtils.toJson(value);
			}
		} catch (ZipException e) {
			value.errorMsg = e.getLocalizedMessage();
			return JsonUtils.toJson(value);
		}
		value.isEncrypted = false;
		return JsonUtils.toJson(value);
	}
	
	/**
	 * 
	 * @param virtualFilePath
	 * @param password
	 * @return
	 */
	public String extractZipFile(String virtualFilePath, String password) {
		ExtractListValue value = new ExtractListValue();
        
		Path virtualWorkDirPath = null;
		try {
			virtualWorkDirPath = this.getUniqueDirPath("save_");
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
			value.errorMsg = e.getLocalizedMessage();
			return JsonUtils.toJson(value);
		}
		value.workDir = virtualWorkDirPath.toAbsolutePath().toString();
		System.out.println(value.workDir);

		ZipFile zipFile = new ZipFile(virtualFilePath);
		try {
			if (zipFile.isEncrypted()) {
				zipFile.setPassword(password.toCharArray());
			}
			zipFile.extractAll(virtualWorkDirPath.toAbsolutePath().toString()); // すべてを解凍
			value.fileArray = this.getFileArray(virtualWorkDirPath);

		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
			value.errorMsg = e.getLocalizedMessage();
			return JsonUtils.toJson(value);
		}		
		return JsonUtils.toJson(value);
	}
	
	/**
	 * ファイルデータを取得します。
	 * @param workDirPath
	 * @param fileName
	 * @return
	 */
	public String getFileData(String workDirPath, String fileName) {
		String filePath = workDirPath + File.separator + fileName;
		System.out.println(filePath);
		
		// Tikaオブジェクトを作成
        Tika tika = new Tika();
		
		ExtractFileValue value = new ExtractFileValue();
		value.base64data = this.getBinaryFileContent(filePath);
		value.filename = fileName;
		value.mimeType = tika.detect(value.filename);

		return JsonUtils.toJson(value);
	}

	private String[] getFileArray(Path path) throws IOException {
		List<String> list = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			System.out.println("Files in the directory:");
			for (Path entry : stream) {
				String fileName = entry.getFileName().toString();
				System.out.println(fileName);
				list.add(fileName);
			}

		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
			throw e;
		}
		return list.toArray(new String[list.size()]);	
	}
	
	private String getBinaryFileContent(String filePath) {
		try (InputStream is = Unzipper.class.getResourceAsStream(filePath)) {
			Path path = Paths.get(filePath);
			byte[] buffer = Files.readAllBytes(path);
			// Base64エンコード
			return Base64.getEncoder().encodeToString(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String saveTargetFile(byte[] content) {
		Path destination;
		try {
			destination = this.getUniqueDirPath("load_");
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
		String loadFile = destination.toAbsolutePath().toString() + File.separator + "load.zip";
		System.out.println(loadFile);

		// ファイルの内容を別ファイルに書き出す
		try (FileOutputStream fos = new FileOutputStream(loadFile)) {
			fos.write(content);
			System.out.println("File written successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loadFile;
	}

	private Path getUniqueDirPath(String prefix) throws IOException {
		// 一時フォルダのベースディレクトリ
		String baseTempDir = System.getProperty("java.io.tmpdir");

		// タイムスタンプを用いてユニークなフォルダ名を生成
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		Path uniqueDirPath = Paths.get(baseTempDir, prefix + timeStamp);

		// フォルダを作成
		Files.createDirectory(uniqueDirPath);
		return uniqueDirPath;
	}

	private String getFileName(Path path) {
		String firstFile = null;
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			System.out.println("Files in the directory:");
			for (Path entry : stream) {
				if (firstFile == null) {
					firstFile = entry.getFileName().toString();
					System.out.println(firstFile);
				} else {
					break;
				}

			}

		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
		System.out.println("");
		return path.toAbsolutePath() + File.separator + firstFile;
	}
}
