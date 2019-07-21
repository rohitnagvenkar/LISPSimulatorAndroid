package com.rncorp.lispcompiler.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Locale;

import android.os.Environment;

public class IOFileHandling {

	StringBuilder string_builder = new StringBuilder();
	String android_path_name = Environment.getExternalStorageDirectory().getPath().toString();
	File directory, file;
	BufferedReader br;
	String current_line, file_content = "";

	public IOFileHandling() {
	}

	public String getFileContent(String file_name, String file_directory) {
		try {
			file_name = file_name.toLowerCase(Locale.US);
			if (!file_name.contains(".txt")) {
				file_name = file_name + ".txt";
			}
			directory = new File(android_path_name + file_directory);
			if (directory.exists()) {
				file = new File(directory, file_name);
				if (file.exists()) {
					br = new BufferedReader(new FileReader(file));
					while ((current_line = br.readLine()) != null) {
						string_builder.append(current_line + "\n");
					}
					br.close();
					file_content = string_builder.toString();
				}
			}
			clear();
		} catch (Exception ex) {

		}
		return file_content;
	}

	public void saveFileContent(String file_name, String file_directory, String data_to_save) {
		try {
			file_name = file_name.toLowerCase(Locale.US);
			if (!file_name.contains(".txt")) {
				file_name = file_name + ".txt";
			}
			directory = new File(android_path_name + file_directory);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			file = new File(directory, file_name);
			if (file.exists()) {
				file.delete();
			}
			FileOutputStream fos = new FileOutputStream(file);
			byte[] byte_path;
			byte_path = new byte[data_to_save.length()];
			byte_path = data_to_save.getBytes();
			fos.write(byte_path);
			fos.flush();
			fos.close();
			clear();
		} catch (Exception ex) {
		}
	}

	public void appendFileContent(String file_name, String file_directory, String data_to_save) {
		try {
			file_name = file_name.toLowerCase(Locale.US);
			if (!file_name.contains(".txt")) {
				file_name = file_name + ".txt";
			}
			directory = new File(android_path_name + file_directory);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			file = new File(directory, file_name);
			String oldText = getFileContent(file_name, file_directory);
			String newText;
			if (oldText.equals("")) {
				newText = data_to_save;
			} else {
				newText = oldText + "" + data_to_save;
			}
			FileOutputStream fos = new FileOutputStream(file);
			byte[] byte_path;
			byte_path = new byte[newText.length()];
			byte_path = newText.getBytes();
			fos.write(byte_path);
			fos.flush();
			fos.close();
			clear();
		} catch (Exception ex) {
		}
	}

	public void clear() {
		string_builder.setLength(0);
	}

}
