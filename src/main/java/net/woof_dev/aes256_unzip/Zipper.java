package net.woof_dev.aes256_unzip;

import java.io.File;
import java.util.List;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

public class Zipper {
	/**
	 * パスワードなし圧縮ファイルを生成
	 * @param targetFile
	 * @param zipFilePath
	 */
	public void compress(File targetFile, String zipFilePath) {
		try {
			// ZipFileオブジェクトを作成
			ZipFile zipFile = new ZipFile(zipFilePath);

			// ZipParametersオブジェクトを作成し、設定を行う
			ZipParameters zipParameters = new ZipParameters();
			zipParameters.setCompressionMethod(CompressionMethod.DEFLATE); // 圧縮方式を指定
			zipParameters.setCompressionLevel(CompressionLevel.NORMAL); // 圧縮レベルを指定

			// ファイルまたはディレクトリをZIPファイルに追加
			zipFile.addFile(targetFile, zipParameters);
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * パスワードあり圧縮ファイルを生成
	 * @param targetFile
	 * @param password
	 * @param zipFilePath
	 */
	public void compress(File targetFile, String password, String zipFilePath) {
		try {
			// ZipFileオブジェクトを作成
			ZipFile zipFile = new ZipFile(zipFilePath, password.toCharArray());

			// ZipParametersオブジェクトを作成し、設定を行う
			ZipParameters zipParameters = new ZipParameters();
			zipParameters.setCompressionMethod(CompressionMethod.DEFLATE); // 圧縮方式を指定
			zipParameters.setCompressionLevel(CompressionLevel.NORMAL); // 圧縮レベルを指定
			zipParameters.setEncryptFiles(true); // ファイルを暗号化する設定
			zipParameters.setEncryptionMethod(EncryptionMethod.AES); // 暗号化方式を指定

			// ファイルまたはディレクトリをZIPファイルに追加
			zipFile.addFile(targetFile, zipParameters);
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * パスワードあり圧縮ファイルを生成(複数ファイルバージョン)
	 * @param targetFileList
	 * @param password
	 * @param zipFilePath
	 */
	public void compress(List<File> targetFileList, String password, String zipFilePath) {
		try {
			// ZipFileオブジェクトを作成
			ZipFile zipFile = new ZipFile(zipFilePath, password.toCharArray());

			// ZipParametersオブジェクトを作成し、設定を行う
			ZipParameters zipParameters = new ZipParameters();
			zipParameters.setCompressionMethod(CompressionMethod.DEFLATE); // 圧縮方式を指定
			zipParameters.setCompressionLevel(CompressionLevel.NORMAL); // 圧縮レベルを指定
			zipParameters.setEncryptFiles(true); // ファイルを暗号化する設定
			zipParameters.setEncryptionMethod(EncryptionMethod.AES); // 暗号化方式を指定

			// ファイルまたはディレクトリをZIPファイルに追加
			for(File targetFile : targetFileList) {
				zipFile.addFile(targetFile, zipParameters);				
			}
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}
	


}
