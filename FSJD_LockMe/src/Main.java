import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class Main {
	public static LinkedList<User> userList = new LinkedList<>();
	private static Scanner scanner = new Scanner(System.in);
	private static Scanner keyboard;
	private static PrintWriter writer;
	private static Map<String, File> fileList = new LinkedHashMap<>();
	private static File f;
    private static User user;	
	

	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		welcomeScreen();
		signInOptions();
	}
	
	public static void welcomeScreen() {
		System.out.println("==========================================");
		System.out.println("           Welcome To LockMe.com	      ");
		System.out.println("==========================================");
		System.out.println("        Your Personal Digital Locker      ");
		System.out.println("             Roshankumar Bisoi            ");
		System.out.println("==========================================");	
	}
	
	public static void signInOptions() throws IOException, ClassNotFoundException {
		System.out.println("1 . REGISTER ");
		System.out.println("2 . LOGIN ");
		System.out.println("3 . EXIT");
		int option = scanner.nextInt();
		switch(option) {
			case 1 : 
				registerUser();
				break;
			case 2 :
				loginUser();
				break;
			case 3 :
				System.out.println("Application closed");
				System.exit(1);
			default :
				System.out.println("Please select correct option");
				System.out.println("                                         ");
				signInOptions();
				break;
		}
		//scanner.close();
	}
	
	public static void signInOptions(boolean registered) throws IOException, ClassNotFoundException {
		System.out.println("1 . LOGIN ");
		System.out.println("2 . EXIT");
		int option = scanner.nextInt();
		switch(option) {
			case 1 :
				loginUser();
				break;
			case 2 :
				System.out.println("Application closed");
				System.exit(1);
			default :
				System.out.println("Please select correct option");
				System.out.println("                                         ");
				signInOptions(true);
				break;
		}
		//scanner.close()
	}
	
	public static void registerUser() throws IOException, ClassNotFoundException {
		System.out.println("==========================================");
		System.out.println("    WELCOME TO REGISTRATION PAGE	 ");
		System.out.println("==========================================");
		while(true) {
			System.out.println("Enter Username :");
			String username = scanner.next().toLowerCase();
			File dir = new File(username);
			boolean isAvail = dir.isDirectory();
			if(isAvail==false) {
				System.out.println("Enter password :");
				String password = scanner.next();
				createUser(username, password);
				f = new File(username);
				f.mkdir();
				System.out.println("User Registration Suscessful !");
				System.out.println("                                         ");
				signInOptions(true);
				break;
			}else {
				System.out.println("User Already Exist! Please Try With Other UserName");
				System.out.println("                                         ");
				continue;
			}

		}
		
	}
	
	public static void loginUser() throws IOException, ClassNotFoundException {
		System.out.println("==========================================");
		System.out.println("   WELCOME TO LOGIN PAGE	 ");
		System.out.println("==========================================");
		while(true) {
			System.out.println("Enter Username :");
			String username = scanner.next().toLowerCase();
			System.out.println("Enter Password");
			String password = scanner.next();
			
			boolean isAvail = isAvailable(username,password);
			if(isAvail == true) {
			       System.out.println("Login Successful !");
			       System.out.println("                                         ");
			       lockerOptions(username);
				   break;
			} else if(isAvail == false){
				System.out.println("Wrong Credentials! Try again.");
				System.out.println("                                         ");
				continue;
			}
		}
	}
	
		
	public static void lockerOptions(String username) throws IOException, ClassNotFoundException {
	    System.out.println("1 . SEE ALL FILES");
		System.out.println("2 . CREATE NEW FILE");
		System.out.println("3 . SEARCH FILE");
		System.out.println("4 . DELETE FILE");
		System.out.println("5 . LOG OUT");
		System.out.println("6 . EXIT");
		int option = scanner.nextInt();
		File dir = new File(username);
		switch(option) {
				case 1 : 
			        int input = showFiles(dir.listFiles());
			        File file = selectedFile(input, dir.listFiles());
			        
			        if(file == null) {
			        	lockerOptions(username);
			        }
			        
			        int processInput = showFileEditOptions();
			        processOptionSearch(processInput,file,username);
					break;
				case 2 :
					System.out.println("Enter a name for file :");
					String fileName = scanner.next();
					f = new File(username,fileName);
					f.createNewFile();
					int fileInput = showFileEditOptions();
					processOption(fileInput, f,username);
					break;
				case 3:
					//System.out.println("Enter name of the file:");
					//String file_name = scanner.next();
					searchFiles(username,dir.listFiles());
				case 4:
					//System.out.println("Enter name of the file:");
					//String file_name1 = scanner.next();
					deleteFile(username,dir.listFiles());
				case 5:
					signInOptions();
				case 6:
					System.out.println("Application closed");
					System.exit(1);
				default :
					System.out.println("Please select from the following options");
					lockerOptions(username);
					break;
			}
		}
		
	public static void createUser(String username, String password) throws IOException {
		File  dbFile = new File("userdatabase.txt");
		writer = new PrintWriter( new BufferedWriter(new FileWriter(dbFile,true)));
		user = new User();
		user.setUsername(username);
		user.setPassword(password);
		writer.println(user);
		writer.close();
		
	}
	
	
		
		
	public static boolean isAvailableRegister(String username,String password) throws IOException, ClassNotFoundException, ClassCastException {
		
			FileInputStream fin = new FileInputStream(new File("userdatabase"));
			ObjectInputStream ois = new ObjectInputStream(fin);
			@SuppressWarnings("unchecked")
			LinkedList<User> uL =  (LinkedList<User>) ois.readObject();
			System.out.println(uL);
			User user = new User(username,password);
			System.out.println("Is Avail :"+uL.contains(user));
			if((uL.contains(user))==true) {
				ois.close();
				System.out.println("true");
				return true;
			} else {
				ois.close();
				System.out.println("false");
				return false;
			}
	}
	
	public static boolean isAvailable(String username, String password) throws IOException, ClassNotFoundException, ClassCastException {
			boolean found = false;
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader("userdatabase.txt"));
			//LinkedList<User> users = new LinkedList<User>();
			String str = "";
	        while((str = br.readLine())!=null){
	          String userdata = "";
	          userdata = str;
	          if((userdata.contains(username)) && userdata.contains(password)) {
	        	  //System.out.println("Found User Data");
	        	  found = true;
	        	  break;
	          } else {
	        	  found = false;
	        	  continue;
	          }
	        }  
	         
			/*;
			if(existedusers.containsKey(username.toLowerCase())) {
				found = true;
			}
			*/
			return found;
	}		
		
	public static void createFile(String fileName,String username) {
		f = new File(username);
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}
		
	public static void editFile(String fileName) {
		File file = fileList.get(fileName);
		try {
			FileWriter fw = new FileWriter(file,true);
			writer = new PrintWriter(fw);
			System.out.println("Write something to your file below :");
			keyboard.nextLine();
			String data = keyboard.nextLine();
			writer.println(data);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
				if(keyboard!=null) {
					 keyboard.close();
		}
		writer.close();
		}
			
	}
		
	public static int searchFiles(String username,File[] files) throws IOException, ClassNotFoundException {
		System.out.println("Enter name of the file:");
		String file_name = scanner.next();
		@SuppressWarnings("unused")
		boolean notFound = false;
	    for (File file : files) {
	    	if ((file.getName()).equals(file_name)) {
	    		System.out.println("File Found !!");
	    		File foundfile = file.getCanonicalFile();
	    		int input = showFileEditOptions();
	    		processOption(input,foundfile,username);
	    	} else {
	    		notFound = true;
	    		continue;
	    	}
	    }
	    if (notFound = true) {
	    	System.out.println("File Not Found Try Again.");
	    	System.out.println("                                         ");
	    	searchFiles(username,files);
	    }
	    int input = scanner.nextInt();
	    return input;
	}
	
	public static void deleteFile(String username,File[] files) throws IOException, ClassNotFoundException {
		System.out.println("Enter name of the file:");
		String file_name = scanner.next();
		@SuppressWarnings("unused")
		boolean notFound = false;
	    for (File file : files) {
	    	if (file.getName().equals(file_name)) {
	    		System.out.println("file found!");
	    		@SuppressWarnings("unused")
				boolean isDeleted = file.delete();
	    		if (isDeleted = true) {
	    			System.out.println("Deleted");
	    			System.out.println("                                         ");
	    		}
	    		int input = showFileEditOptions();
	    		processOption(input,file,username);
	    		break;
	    	} else {
	    		notFound = true;
	    		continue;
	    	}
	    }
	    if (notFound = true) {
	    	System.out.println("File Not Found Try Again.");
	    	System.out.println("                                         ");
	    	deleteFile(username,files);
	    }
	    
	}
	
	
	public static int showFiles(File[] files) {
		int index = 0;
		System.out.println("Select a file.");
	    for (File file : files) {
	        index = index+1; 
	        System.out.println(index +" : "+ file.getName());
	    }
	    System.out.println((index+1)+" : GO BACK");
	    int input = scanner.nextInt();
	    return input;
	}
	
    
	public static File selectedFile(int input, File[] files) {
		if(input<(files.length+1)) {
			return files[input-1];
		} else {
			return null;
		}
	}
	
	
	public static int showFileEditOptions() {
		System.out.println("Select option :");
		System.out.println("1. Read");
		System.out.println("2. Write");
		System.out.println("3. Go Back");
		System.out.println("4. Exit");
		int fileOption = scanner.nextInt();
		return fileOption;
	}
	
	public static void readFile(File file,String username) throws ClassNotFoundException {
		FileReader fr = null;
		try {
			System.out.println("=====================================================");
			System.out.println("                                                     ");
			fr = new FileReader(file);
			int i;    
			while((i=fr.read())!=-1)    
			System.out.print((char)i);    
			System.out.println("                                                     ");
			System.out.println("=====================================================");
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fr.close();
				int processInput = showFileEditOptions();
		        processOptionSearch(processInput,file,username);
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	public static void writeIntoFiles(File file, String username) throws ClassNotFoundException {
		//Writing into file
		System.out.println("1. Replace");
		System.out.println("2. Append");
		int userInput = scanner.nextInt();
		writer = null; 
		try {
			FileWriter fw = null;
			if(userInput == 1) {
				fw = new FileWriter(file);
			} else {
				fw = new FileWriter(file, true);
			}
			writer = new PrintWriter(new BufferedWriter(fw));
			System.out.println("Write something into file : "+file.getName());
			//System.out.println("File Name : "+file.getName());
			scanner.nextLine();
			String data = scanner.nextLine();
			writer.println(data);
			writer.flush();
			System.out.println("You have successfully written into a file");
			System.out.println("                                         ");
			int processInput = showFileEditOptions();
	        processOptionSearch(processInput,file,username);
		} catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(keyboard!=null)
				 keyboard.close();
			writer.close();
		}
		//writer.close();
		
	}
	
	
	
	public static void processOption(int input, File file, String username) throws IOException, ClassNotFoundException {
		if (input == 1) {
			System.out.println("File : "+file.getName());
			readFile(file,username);
		} else if(input == 2) {
			writeIntoFiles(file,username);
		} else if(input == 3) {
			lockerOptions(username.toLowerCase());
		} else {
			System.out.println("Application closed");
			System.exit(1);
		}
	}
	
	public static void processOptionSearch(int input, File file, String username) throws IOException, ClassNotFoundException {
		if (input == 1) {
			readFile(file,username);
		} else if(input == 2) {
			writeIntoFiles(file,username);
		} else if(input == 3) {
			File dir = new File(username.toLowerCase());
			int inputfiles = showFiles(dir.listFiles());
	        File filepage = selectedFile(inputfiles, dir.listFiles());
	        if(filepage == null) {
	        	lockerOptions(username);
	        }
	        int processInput = showFileEditOptions();
	        processOptionSearch(processInput,file,username);
		} else {
			System.out.println("Application closed");
			System.exit(1);
		}
	}
		
}

