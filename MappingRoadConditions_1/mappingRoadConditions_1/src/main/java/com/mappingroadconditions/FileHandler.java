package com.mappingroadconditions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import android.content.Context;
//import android.util.Log;

public class FileHandler {

	/*synchronized String ReadFile() {
		if (!MainActivity.hasExternalStorage) {
			try {
				String FILENAME = "features";
				FileInputStream fileinputstream = MainActivity.context_
						.openFileInput(FILENAME);
				int size = 0;

				size = (int) fileinputstream.getChannel().size();
				byte[] bytes = new byte[size];
				fileinputstream.read(bytes);
				String Data = new String(bytes, "UTF-8");
				// Log.i("MappingRoadConditions", "Features: " + new
				// String (bytes, "UTF-8"));
				fileinputstream.close();
				MainActivity.context_.deleteFile(FILENAME);
				return Data;

			} catch (FileNotFoundException e) {
				Log.i("MappingRoadConditions", e.toString());
				e.printStackTrace();
				return " ";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return " ";
			}
		} else {
			// use external memory

			// Get the directory for the user's public pictures directory.
			File root = android.os.Environment.getExternalStorageDirectory();
			File dir = new File(root.getAbsolutePath()
					+ "/MappingRoadConditions");
			Log.i("MappingRoadConditions", dir.getAbsolutePath());
			if (!dir.exists()) {
				Log.e("MappingRoadConditions", "Directory not created");
				return " ";
			} else {
				File text = new File(dir, "Features.txt");
				if (!text.exists())
					return " ";
				try {
					FileInputStream f = new FileInputStream(text);
					int size = (int) f.getChannel().size();
					byte[] bytes = new byte[size];
					f.read(bytes);
					String data = new String(bytes, "UTF-8");
					f.close();
//empty the file
					try {
						text.delete();
						Log.i("MappingRoadConditions","Old file deleted: "+ text.exists());
						} catch (Exception e) {

						Log.i("MappingRoadConditions", "File not found.");
					}
					return data;

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return " ";
		}

	}

	synchronized void WriteFile(String string) {

		Log.i("MappingRoadConditions", "In write function :D");
		if (!MainActivity.hasExternalStorage) {
			String FILENAME = "features";
			FileOutputStream outputStream;

			try {

				outputStream = MainActivity.context_.openFileOutput(FILENAME,
						Context.MODE_APPEND);

				outputStream.write(string.getBytes());
				outputStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Log.i("MappingRoadConditions", "In Write File");
			String featuresfile = "Features.txt";
			File root = android.os.Environment.getExternalStorageDirectory();
			File dir = new File(root.getAbsolutePath()
					+ "/MappingRoadConditions");

			if (!dir.exists()) {
				dir.mkdirs();
				Log.e("MappingRoadConditions", "write directory newly created");

			} else
				Log.e("MappingRoadConditions", "Directory exists");

			File file = new File(dir, featuresfile);
			Log.i("MappingRoadConditions", file.getAbsolutePath());

			try {
				FileOutputStream f = new FileOutputStream(file, true);
				OutputStreamWriter myOutWriter = new OutputStreamWriter(f);
				myOutWriter.append(string);
				myOutWriter.close();
				f.close();
			} catch (Exception e) {

				Log.i("MappingRoadConditions",
						"******* File not found. Did you"
								+ " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
			}
		}
	}*/
    synchronized String ReadFile() {
        if (!MainActivity.hasExternalStorage) {
          System.out.println(  "In Read File");
            try {
                String FILENAME = "classified";
                FileInputStream fileinputstream = MainActivity.context_
                        .openFileInput(FILENAME);
                int size = 0;

                size = (int) fileinputstream.getChannel().size();
                byte[] bytes = new byte[size];
                fileinputstream.read(bytes);
                String Data = new String(bytes, "UTF-8");
                // Log.i("MappingRoadConditions", "Features: " + new
                // String (bytes, "UTF-8"));
                fileinputstream.close();
                MainActivity.context_.deleteFile(FILENAME);
                return Data;

            } catch (FileNotFoundException e) {
              //  Log.i("MappingRoadConditions", e.toString());
                System.out.println(e.toString());
                e.printStackTrace();
                return " ";
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return " ";
            }
        } else {
            // use external memory

            // Get the directory for the user's public pictures directory.
            File root = android.os.Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath()
                    + "/MappingRoadConditions");
         //   Log.i("MappingRoadConditions", dir.getAbsolutePath());
            if (!dir.exists()) {
          //      Log.e("MappingRoadConditions", "Directory not created");
                return " ";
            } else {
                File text = new File(dir, "Classified.txt");
                if (!text.exists())
                    return " ";
                try {
                    FileInputStream f = new FileInputStream(text);
                    int size = (int) f.getChannel().size();
                    byte[] bytes = new byte[size];
                    f.read(bytes);
                    String data = new String(bytes, "UTF-8");
                    f.close();
//empty the file
                    try {
                        text.delete();
                  //      Log.i("MappingRoadConditions","Old file deleted: "+ text.exists());
                    } catch (Exception e) {
;
                    //    Log.i("MappingRoadConditions", "File not found.");
                    }
                    return data;

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return " ";
        }

    }

    synchronized void WriteFile(String string) {

     //   Log.i("MappingRoadConditions", "In write function :D");
        if (!MainActivity.hasExternalStorage) {
            String FILENAME = "classified";
            FileOutputStream outputStream;

            try {

                outputStream = MainActivity.context_.openFileOutput(FILENAME,
                        Context.MODE_APPEND);

                outputStream.write(string.getBytes());
                outputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
       //     Log.i("MappingRoadConditions", "In Write File");
            System.out.println("\"In Write File\"");
            String featuresfile = "Classified.txt";
            File root = android.os.Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath()
                    + "/MappingRoadConditions");

            if (!dir.exists()) {
                dir.mkdirs();
             //   Log.e("MappingRoadConditions", "write directory newly created");

            } else ;
               // Log.e("MappingRoadConditions", "Directory exists");

            File file = new File(dir, featuresfile);
         //   Log.i("MappingRoadConditions", file.getAbsolutePath());

            try {
                FileOutputStream f = new FileOutputStream(file, true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(f);
                myOutWriter.append(string);
                myOutWriter.close();
                f.close();
            } catch (Exception e) {

          /*      Log.i("MappingRoadConditions",
                        "******* File not found. Did you"
                                + " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
          */ ;  }
        }
    }

}
