package net.woof_dev.aes256_unzip;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ZipperTest {

	@Test
	public void test_pass_nashi() {
		Zipper zipper = new Zipper();
		zipper.compress(this.getFile("５種キノコのペペロンチーノ.pdf"), "sample-pass-nashi.zip");
	}

	@Test
	public void test_pass_ari() {
		String password = "passw0rd";
		Zipper zipper = new Zipper();
		zipper.compress(this.getFile("アンチョビキャベツのパスタ.pdf"), password, "sample-pass-ari.zip");
	}

	@Test
	public void test_multi() {
		String password = "passw0rd";
		List<File> fileList = new ArrayList<>();
		fileList.add(this.getFile("アマトリチャーナ材料.JPG"));
		fileList.add(this.getFile("アマトリチャーナ.JPG"));
		fileList.add(this.getFile("ボンゴレビアンコ材料.JPG"));
		fileList.add(this.getFile("ボンゴレビアンコ.JPG"));
		
		Zipper zipper = new Zipper();
		zipper.compress(fileList, password, "sample-multi.zip");
	}

	private File getFile(String fileName) {
		Path curDir = Paths.get("");
	    Path projectDir = curDir.toAbsolutePath();

	    System.out.println(projectDir.toString());
	    String path = String.format("%s/src/test/resources/%s", projectDir.toString(), fileName);
	    File file = new File(path);
	    return file;
	}}
