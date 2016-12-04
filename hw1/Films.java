package hw1;
import java.io.*; 

//package com.thread.multipl.mysolution;  
  
import java.io.IOException;  
import java.io.RandomAccessFile;  
import java.util.concurrent.CountDownLatch;  

import java.util.*;
import java.io.*;
import java.text.*;

public class Films {
	//ArrayList<Database> list = new ArrayList<Database>();
	ArrayList<Database> list;
	boolean JDBG = false;
	//boolean JDBG = true;
	int cnt = 0;
	String search_arg = "";
	int update_type = 0;
	String out;
	int MM = 0;
	int DD = 0;
	int YY = 0;
	
	//public static void main(String[] args) {
	public static void main(String[] args) {

		Films hw1 = new Films();
		hw1.list = new  ArrayList<Database>();		
		
		//Films hw1 = new Films();
		//hw1.list = null;
		
		// Prints "Hello, World" in the terminal window.
		//System.out.println("Hello, World");	
		try{
			writeFile1("null", false);
		}catch(IOException e){
			System.out.println("ERROR: write file failed");
		}
		
		try{
			hw1.read();
		}catch(IOException ee){
			;
		}
	}

	//public static void read(String[] args) {
	//public static void read() {
	public void read() throws IOException {
		//ArrayList<Database> list = new ArrayList<Database>();
		//ArrayList<Database> list = new ArrayList<Database>();		
		//ArrayList<String> list = new ArrayList(Arrays.asList("Ryan", "Julie", "Bob"));
		//String str = new String();
		//list.add(str);
		
		String w_buf; 
		String fileName ="in.txt";
		
		File currentDir = new File("").getAbsoluteFile();
		System.out.println(currentDir);
		//String currentDir = System.getProperty("user.dir");
		//System.out.println("Current dir using System:" +currentDir);

		String line;
		try {
			// use BufferedReader and apply on FileReader. process streaming apply on node streaming
			BufferedReader in = new BufferedReader(new FileReader(currentDir+"/"+fileName));

			// read the first line 
			line = in.readLine();
			// if null
			while(line!=null)
			{
			

			
				int p=1;
				while(p==1) { 
					p=0;
					// output one entire line from in.txt for debuging
					//System.out.println(line);
					
					//Parsing Strings with split
					//http://pages.cs.wisc.edu/~hasti/cs302/examples/Parsing/parseparseString.html
					String line22 = "";
					//String line22 = new String;
					String delims_space = "[ ]+"; //target: "the it   hard        concentrate";
					String[] tokens = line.split(delims_space);
					String[] args = new String[100];
					for (int i = 1; i < tokens.length; i++) {
						//System.out.println(tokens[i]);
						line22 += tokens[i];
						args[i] = tokens[i];
					}
					
					System.out.println("************************" + tokens[0] +  "**********************************");
					
					String delims_a = "[\"]"; //target: 
					String[] tokens2 = line22.split(delims_a);
					for (int i = 1; i < tokens2.length; i++) {
						//System.out.println(tokens2[i]);
						;
					}
					
					String[] tokens_arg = new String[100];
					//if(tokens[0].equals("ADD") || tokens[0].equals("UPDATE")) {
					if(tokens[0].equals("ADD")) {
						int count_Dquote = count_(line);
						if(JDBG) { System.out.println(count_Dquote); }
						
						if(count_Dquote==0) {	
							if (tokens[0].equals("ADD"))
								if(tokens.length!=5) { my_exception("ADD: ERROR WRONG_ARGUMENT_COUNT"); break; }
							//if (tokens[0].equals("UPDATE"))
							//	if(tokens.length!=4) { System.out.println("[WARN] ADD: len WRONG"); return;}
						
							tokens_arg[0] = tokens[0];
							tokens_arg[1] = tokens[1];
							tokens_arg[2] = tokens[2];
							tokens_arg[3] = tokens[3];
							if (tokens[0].equals("ADD")) tokens_arg[4] = tokens[4];
							//if (tokens[0].equals("UPDATE")) ;

						}
						else if(count_Dquote==2) {
							if (tokens[0].equals("ADD"))
								if(tokens.length!=6) { my_exception("ADD: ERROR WRONG_ARGUMENT_COUNT"); break;}
							//if (tokens[0].equals("UPDATE"))
							//	if(tokens.length!=5) { System.out.println("[WARN] ADD: len WRONG"); return;}
							
							
							//Find the first second "\""
							String target_str = "\"";
							int count1 = 0;
							int start = 0;
							int start1 = 0;
							int start2 = 0;

							while (line.indexOf(target_str, start) >= 0 && start < line.length()) {
								count1++;
								start = line.indexOf(target_str, start) + target_str.length();
								if(start1==0)
									start1 = line.indexOf(target_str, 0) + target_str.length();
								else 
									start2 = line.indexOf(target_str, start1) + target_str.length();
							}
							if(JDBG) {
                                System.out.println("count1="+count1);
	    						System.out.println("start1="+start1+"   "+"start2="+start2);
                            }
							//Find the first second space
							String target_str_ = " ";
							int count1_ = 0;
							int start_ = 0;
							int start1_ = 0;
							int start2_ = 0;
							while (line.indexOf(target_str_, start_) >= 0 && start_ < line.length()) {
								count1_++;
								start_ = line.indexOf(target_str_, start_) + target_str_.length();
								if(start1_==0)
									start1_ = line.indexOf(target_str_, 0) + target_str_.length();
								else 
									start2_ = line.indexOf(target_str_, start1_) + target_str_.length();
								if (count1_==2) break;
							}
                            if(JDBG) {
							    System.out.println("count1_="+count1_);
							    System.out.println("start1_="+start1_+"   "+"start2_="+start2_);
                            }
							if(start1_+1 == start1) {
								//front
                                if(JDBG) {
								    System.out.println("FRONT");
								    System.out.println("FRONT");
								    System.out.println("FRONT");
                                }
								tokens_arg[0] = tokens[0];
								tokens_arg[1] = tokens[1] + " " + tokens[2];
								tokens_arg[2] = tokens[3]; 
								tokens_arg[3] = tokens[4];
								if (tokens[0].equals("ADD")) tokens_arg[4] = tokens[5];
								//if (tokens[0].equals("UPDATE")) ;

								
							}
							else {
								//back
                                if(JDBG) {
							    	System.out.println("BACK");
							    	System.out.println("BACK");
								    System.out.println("BACK");
                                }
								tokens_arg[0] = tokens[0];
								tokens_arg[1] = tokens[1];
								tokens_arg[2] = tokens[2] + " " + tokens[3]; 
								tokens_arg[3] = tokens[4];
								if (tokens[0].equals("ADD")) tokens_arg[4] = tokens[5];
								
							}
						}
						else if(count_Dquote==4) {
							if (tokens[0].equals("ADD"))
								if(tokens.length!=7) { my_exception(tokens[0] + ": ERROR WRONG_ARGUMENT_COUNT"); break;}
							//if (tokens[0].equals("UPDATE"))
							//	if(tokens.length!=6) { System.out.println("[WARN] ADD: len WRONG"); return;}
							
							tokens_arg[1] = tokens[1] + " " + tokens[2];
							tokens_arg[2] = tokens[3] + " " + tokens[4];
							tokens_arg[3] = tokens[5];
							if (tokens[0].equals("ADD")) tokens_arg[4] = tokens[6];
							//if (tokens[0].equals("UPDATE")) ;

						}
						if(JDBG) {
							System.out.println("TOKENS_ARG1:" + tokens_arg[1]);
							System.out.println("TOKENS_ARG2:" + tokens_arg[2]);
							System.out.println("TOKENS_ARG3:" + tokens_arg[3]);
							if (tokens[0].equals("ADD")) System.out.println("TOKENS_ARG4:" + tokens_arg[4]);
							//if (tokens[0].equals("UPDATE")) ;
						}
					}

					if(tokens[0].equals("SEARCH")) {
						search_arg = line.replace("SEARCH ", "");
					}
					
					if(tokens[0].equals("UPDATE")) {
						/*
						//////
							while (line.indexOf(target_str, start) >= 0 && start < line.length()) {
								count1++;
								start = line.indexOf(target_str, start) + target_str.length();
								if(start1==0)
									start1 = line.indexOf(target_str, 0) + target_str.length();
								else 
									start2 = line.indexOf(target_str, start1) + target_str.length();
							}
							int count = 0; int start = 0;
							while (str.indexOf(str1, start) >= 0 && start < str.length()) {
								count++;
								start = str.indexOf(str1, start) + str1.length();
							}
							*/
							///////////////
						//check tokens.length!=6

							//Find the first second "\""
							String target_str = "\"";
							int count1 = 0; //0 2 4
							int start_pos=0;
							int start[] = new int[8];

							while (line.indexOf(target_str, start_pos) >= 0 && start_pos < line.length()) {
								start_pos = line.indexOf(target_str, start_pos) + target_str.length();
								start[count1] = start_pos-1;
								if(JDBG) { System.out.println("start[" + count1+ "] =" + start_pos); }
								count1++;
							}
							//System.out.println(start[count1]);
							//System.out.println("start[count1]="+start[count1]+"   "+"start2="+start2);	

							String update_arg[] = new String[4];
							char[] charArray = line.toCharArray();
							cnt=0;
							if(JDBG) { System.out.println("count1 =" + count1); }
							if (count1!=0) { //is double quotes
								for (int k=0 ; k<count1 ; k=k+2, cnt++) {
									update_arg[cnt]="";
									for (int z=start[k]; z<=start[k+1]; z++) {
										update_arg[cnt] = update_arg[cnt] + charArray[z];
									}
									update_arg[cnt].replace("\"","");
									if(JDBG) { System.out.println("update_arg[" + cnt + "] " + update_arg[cnt]); }
									//update_arg[cnt] +=  "\n";
								}
							}
							
							cnt=0;
							//int cnt_space=0;
							for (int h=0 ; h<line.length(); h++) {
								//cnt_space=0;
								// ' '
								if(charArray[h]==' ') {
									cnt++; 
									continue;
								}
								// '\"'    TODO :DEBUG '"' OR '\"'
								if(charArray[h]=='\"') {
									for (int q=h+1 ; q<line.length(); q++) {
										//cnt_space++;
										h++;
										if(charArray[q]=='\"')
											break;
										
										if(q==line.length()-1) {
											System.out.println("ERROR: single d_quotes");	
										}
									}
									//System.out.println("cnt_space=" + cnt_space);	
									//cnt_space++;
									//h++;
									//h = h+cnt_space;
									
								}
								//others: do nothing
							}
							cnt++;
							if(JDBG) { System.out.println("Total chunks = cnt =" + cnt + "  !!!"); }
							
							
							
							//nothing	
								int cnt2=0; 
								int old_idx = 0;
								int q_idx1 = 0;
								int q_idx2 = 0;
								int is_quotes = 0;
								String update_arg2[] = new String[5];
								//int cnt_space=0;
								for (int h=0 ; h<line.length(); h++) {
									//cnt_space=0;
									// ' '
									is_quotes=0;
									if(charArray[h]==' ') {
										//
										update_arg2[cnt2]=""; //init
										for (int qq=old_idx ; qq<h; qq++) {
											update_arg2[cnt2] += charArray[qq];
										}
										cnt2++;
										old_idx = h+1;
										//continue;
									}
									// '\"'    TODO :DEBUG '"' OR '\"'
									else if(charArray[h]=='\"') {
										q_idx1 = h;
										for (int q=h+1 ; q<line.length(); q++) {
											//cnt_space++;
											h++;
											is_quotes=1;
											if(charArray[q]=='\"') {
												q_idx2=q;
												break;
											}
											//if(q==(line.length()-1)) {
											if(q==(line.length())) {
												System.out.println("ERROR: single d_quotes");	
											}
										}
										update_arg2[cnt2]=""; //init
										for (int qq=q_idx1 ; qq<=q_idx2; qq++) {
											update_arg2[cnt2] += charArray[qq];
										}
										old_idx = q_idx2+2;
										cnt2++;
										h+=1; // skip @" @ // when next iter, it will add by 1 immediately
										//h+=1; // skip @" @
										if(JDBG) {
											System.out.println("old_idx = " + old_idx + " q_idx1 = "+ q_idx1 + 
																" q_idx2 = "+ q_idx2 +"  " + "h = " + h +
																" line.length() = " + line.length()	);
										}
										//old_idx = h++;
									}
									else ; //others: do nothing
								}
								//last one
								if(cnt==5) {
									if(JDBG) {
										System.out.println("@@@@@:::::: old_idx = " + old_idx + " q_idx1 = "+ q_idx1 + 
															" q_idx2 = "+ q_idx2 +"  " + 
															" line.length() = " + line.length()	);
									}
									update_arg2[4]="";
									//second last = ""
									if(is_quotes==1) {
										for (int ff=q_idx1 ; ff<line.length(); ff++) {
												update_arg2[4] += charArray[ff];
												if(JDBG) { System.out.println("@@@@@@"+ff+":" + charArray[ff] + "   limit" + line.length()); }
										}
									}
									else {//second last  = normal // =is_quotes==0
										for (int ff=old_idx ; ff<line.length(); ff++) {
												update_arg2[4] += charArray[ff];
												if(JDBG) { System.out.println("@@@@@@"+ff+":" + charArray[ff] 
																				+ "   limit" + line.length()); }
										}
									}
								}
								else if(cnt==4) {
									update_arg2[3]="";
									for (int ff=old_idx ; ff<line.length(); ff++) {
											update_arg2[3] += charArray[ff];
									}
								}
								
								
								//cnt++; // we count space, but dhunks = space+1
								//if(JDBG) { System.out.println("Total chunks = cnt =" + cnt + "  !!!222"); }
								if(JDBG) {
									for (int ww=0 ; ww<cnt; ww++) {
										System.out.println("update_arg2["+ww+"] = " + update_arg2[ww]);
									}
								}
								
								if(cnt==5) {
									if(JDBG) { System.out.println("UPDATE title + director"); }
									if(JDBG) { System.out.println("UPDATE title + director"); }
									if(JDBG) { System.out.println("UPDATE title + director"); }
									tokens_arg[1] = update_arg2[1]; //old title 
									tokens_arg[2] = update_arg2[2]; //old title 
									tokens_arg[3] = update_arg2[3]; //new title 
									tokens_arg[4] = update_arg2[4]; //new director
									update_type = 3; // chang title + director
								}
								else if(cnt==4) {
									if(JDBG) { System.out.println("UPDATE DAY or SEEN"); }
									if(JDBG) { System.out.println("UPDATE DAY or SEEN"); }
									if(JDBG) { System.out.println("UPDATE DAY or SEEN"); }
									tokens_arg[1] = update_arg2[1]; //old title 
									tokens_arg[2] = update_arg2[2]; //old title 						
									tokens_arg[3] = update_arg2[3]; // new day or new seen
									//tokens_arg[4] = update_arg2[4];
									update_type = 4; // chang day or seen
								}
								else {
									my_exception("UPDATE: ERROR WRONG_ARGUMENT_COUNT"); 
									break;
								}
							//nothing
					} //UPDATE end
				
					
					
		
					
					
					// break "line" down
					//String head = head(line);
					//parse: check command and then args
					
					switch(tokens[0])
					{
						case "CLEAR":
								//stringArray one array
								if(JDBG) { System.out.println(list.size()); }
								//
								list.clear();
								/*
								for (int qq=(list.size()-1); qq>=0; qq--) {
									Database get12 = list.get(qq);
									get12=null;
									//Database get12 = ;
									//get12.remove(0);
								}
								*/
								out="CLEAR: OK";
								System.out.println(out);
								writeFile1(out, true);
								break;
						case "ADD":
								
								// check [4] Seen
								if( !tokens_arg[4].equals("true") && !tokens_arg[4].equals("false") ) {
									my_exception(tokens[0] + ": ERROR NOT_BOOL");
									break;
								}

								// check if new name existing then break
								int err_code=0;
								String tokens_add_tmp1= tokens_arg[1].replace("\"","");
								String tokens_add_tmp2= tokens_arg[2].replace("\"","");
								for (int jjjj=0 ; jjjj<list.size() ; jjjj++) {
									Database old3 = list.get(jjjj);
									if(	tokens_add_tmp1.equals(old3.Movie) &&
										tokens_add_tmp2.equals(old3.Direct)) {
										my_exception("ADD: ERROR DUPLICATE_ENTRY");
										err_code=1; break;
									}
								}
								if(err_code==1) { break; }
								
								// check [3] date
								//2 date
								//check number of date
								String delims_c = "[/]"; //target: 
								String[] tokens_date1 = tokens_arg[3].split(delims_c);
								//for (int i = 1; i < tokens_date1.length; i++) {
								//	if(JDBG) { System.out.println(tokens_date1[i]);}
								//}
								if (tokens_date1.length!=3) {
									my_exception("ADD: ERROR INVALID_DATE");
									break;
								}
								//ADD CMD needs to check more
								//

								try {
									  MM = Integer.parseInt(tokens_date1[0]);
									  DD = Integer.parseInt(tokens_date1[1]);
									  YY = Integer.parseInt(tokens_date1[2]);
								} catch (NumberFormatException e) {
									  //No problem this time, but still it is good practice to care about exceptions.
									  //Never trust user input :)
									  //Do something! Anything to handle the exception.
									  my_exception("ADD: ERROR INVALID_DATE");
									  break;
								}
								
								//general check
								if( MM<=0 || DD<=0 || YY<=0 ) {
									my_exception("ADD: ERROR INVALID_DATE");
									break;
								}

								//MM
								if ( MM>12 ) {
									my_exception("ADD: ERROR INVALID_DATE");
									break;
								}
								
								//DD
								if ( (MM/2)==0 ) {
									if( DD>30) {
										my_exception("ADD: ERROR INVALID_DATE");
										break;
									}
								}
								else {
									if( DD>31) {
										my_exception("ADD: ERROR INVALID_DATE");
										break;
									}
								}
								
								//Feb
								if ( (MM)==2 ) {
									if( (YY/400)==0 || ((YY/100)!=0&&(YY/4)==0)  ) {
										//both 4 & 100
										if( DD>29 ) {
											my_exception("ADD: ERROR INVALID_DATE");
											break;
										}
									}
									else {
										if( DD>28 ) {
											my_exception("ADD: ERROR INVALID_DATE");
											break;
										}
									}
								}
								
								//Passed all checks
								// checking date done
								////////////////////////////////////////////////////////////
								
								
								//int count_Dquote = count_(line);
								//System.out.println(count_Dquote);
								//if(count_Dquote==0) {	
									//init and sort
									//Database added = new Database("a", "b", "c");
									//void DataBase(String date, String last, String first)
									
									//save & check tokens[4] (1/0)
									
									Database added = new Database();
									//get_tmp1.Movie = get_tmp.Movie.replace("\"","");
									//get_tmp1.Direct = get_tmp.Direct.replace("\"","");
									//get_tmp1.Date = get_tmp.Date.replace("\"","");
								
									added.DataBase(tokens_arg[1].replace("\"",""),tokens_arg[2].replace("\"",""),tokens_arg[3].replace("\"",""),tokens_arg[4].replace("\"","")); 
									list.add(added);
									if(JDBG) {
										Database get1 = list.get(list.size()-1);
										System.out.println("ADD1:" + get1.Movie);
										System.out.println("ADD2:" + get1.Direct);
										System.out.println("ADD3:" + get1.Date);
										System.out.println("ADD4:" + get1.Seen);
									}	
								/*
								}
								else if(count_Dquote==2) {
									//front 
									
									//back
									
								}
								else if(count_Dquote==4) {
								
								}
								*/
								
								out="ADD: OK ";
								if(JDBG) { System.out.println(out);}
								w_buf="ADD: OK " + tokens_arg[1] + " " + tokens_arg[2];
								System.out.println(w_buf);
								//w_buf= "ADD: OK " + "\""+ tokens_arg[1] + "\" \"" + tokens_arg[2] + "\""; 
								writeFile1(w_buf, true); //++ <title> <director>
	
								break;
						case "SHOW": 
								
								if(tokens.length!=1) {
									my_exception("SHOW: ERROR WRONG_ARGUMENT_COUNT");
									break;
								}
								
								//Count the number of not seen movies
								cnt=0;
								for (int j=0 ; j<list.size() ; j++) { 
								    if(JDBG) { System.out.println(j+":"); }
									Database get_tmp = list.get(j);
									//System.out.println("SHOW4" + get_tmp.Seen);
									if (get_tmp.Seen.equals("false")) {
										cnt++;
									}
								}
								
								out="SHOW: OK " + cnt;
								System.out.println(out);
								writeFile1(out, true);	//++ <num>
														//<title1>,<director1>,<releasedate1>
								if(JDBG) {
									System.out.println("Jack SHOW total size = " + list.size()); 
								}
										
								for (int j=0 ; j<list.size() ; j++) { 
									if(JDBG) { System.out.println(j+":"); }
									Database get_tmp = list.get(j);
									//System.out.println("SHOW4" + get_tmp.Seen);
									if (get_tmp.Seen.equals("false")) {
										if(JDBG) {
											System.out.println("SHOW1" + get_tmp.Movie);
											System.out.println("SHOW2" + get_tmp.Direct);
											System.out.println("SHOW3" + get_tmp.Date);
											System.out.println("SHOW4" + get_tmp.Seen);
										}
										//Database get_tmp1 = new Database();
										//get_tmp1.Movie = get_tmp.Movie.replace("\"","");
										//get_tmp1.Direct = get_tmp.Direct.replace("\"","");
										//get_tmp1.Date = get_tmp.Date.replace("\"","");
										//get_tmp1.Seen = get_tmp.Seen.replace("\"","");

										out="";
										//out = get_tmp1.Movie + "," + get_tmp1.Direct + "," + get_tmp1.Date;
										out = get_tmp.Movie + "," + get_tmp.Direct + "," + get_tmp.Date;
										writeFile1(out, true);
									}
									else {
										if(JDBG) { System.out.println("Seen=true: dont show"); }
									}
								}
			
								break;
						case "UPDATE": 
								int ok=0;
									/*
									update_type = 3; // chang title + director
									tokens_arg[1] = update_arg2[1]; //old title 
									tokens_arg[2] = update_arg2[2]; //old title 
									tokens_arg[3] = update_arg2[3]; //new title 
									tokens_arg[4] = update_arg2[4]; //new director
									
									update_type = 4; //chang day or seen
									tokens_arg[1] = update_arg2[1]; //old title 
									tokens_arg[2] = update_arg2[2]; //old title 
									tokens_arg[3] = update_arg2[3]; // new day or new seen
									*/
									/*
									System.out.println("ADD1:" + old.Movie);
									System.out.println("ADD2:" + old.Direct);
									System.out.println("ADD3:" + old.Date);
									System.out.println("ADD4:" + old.Seen);
									*/
								for (int jj=0 ; jj<list.size() ; jj++) {
									if(JDBG) { System.out.println("UPDATE"+jj+":"); }
									Database old = list.get(jj);
									
									String[] tokens_t = new String[10];
									tokens_t[1] = tokens_arg[1].replace("\"","");
									tokens_t[2] = tokens_arg[2].replace("\"","");
									tokens_t[3] = tokens_arg[3].replace("\"","");
									if(update_type == 3) {
										tokens_t[4] = tokens_arg[4].replace("\"","");
									}
									
									if(JDBG) { 
										System.out.println(tokens_t[1]+"=?"+ old.Movie+"@");
										System.out.println(tokens_t[2]+"=?"+ old.Direct+"@");
									}

									
									if(	tokens_t[1].equals(old.Movie) &&
										tokens_t[2].equals(old.Direct)) 
									{
										if(JDBG) { System.out.println("match match match match!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"); }
										//ok=1;
										//UPDATE
										if(JDBG) { System.out.println("UPDATE: MATCH idx=" + jj); }
										
										if(update_type == 3) {
											//New is existing
											//if(new existing ){
											//if(new existing ){
											//if(new existing ){
											//TODO TODO TODO 
											// CHECK NEW is OLD?
											if(1==0){
												//TODO: NEW is old then come in
												my_exception("UPDATE: ERROR WRONG TYPE");
												break;
											}
											
											// check if new name existing then break
											int error_code2=0;
											for (int jjj=0 ; jjj<list.size() ; jjj++) {
												Database old2 = list.get(jjj);
												if(	tokens_t[3].equals(old2.Movie) &&
													tokens_t[4].equals(old2.Direct)) {
													my_exception("UPDATE: ERROR DUPLICATE_ENTRY");
													error_code2=1; break;
												}
											}
											if(error_code2==1) {
												ok=2; break;
											}
											
											//Pass all checks!! launch commend.
											old.A(tokens_t[3]);
											old.B(tokens_t[4]);
											if(JDBG) { System.out.println("!!!!!!!!!!!!!"+tokens_t[4]); }

											//Database modification = new Database();
											//modification.DataBase(tokens_t[3], tokens_t[4], old.Date, old.Seen); 
											//list.set(jj,modification);//modify index n 
											out="UPDATE: OK " + tokens_t[3] + " " + tokens_t[4];
											ok=1;
										}
										else if(update_type == 4) {
											//check seen / check date
											if (tokens_t[3].equals("true") || tokens_t[3].equals("false") ) {
												//check seen
												
												// Passed all checks 
												old.D(tokens_t[3]);
											}
											else { //2 date
												//check number of date
												String delims_b = "[/]"; //target: 
												String[] tokens_date = tokens_t[3].split(delims_b);
												for (int i = 1; i < tokens_date.length; i++) {
													if(JDBG) { System.out.println(tokens_date[i]);}
												}
												if (tokens_date.length!=3) {
													my_exception("UPDATE: ERROR INVALID_DATE_OR_BOOL");
													ok=2; break;
												}
												//ADD CMD needs to check more
												
												// check date formate
												if (JDBG) {
													System.out.println(tokens_date[0] + "      " + tokens_date[0].length());
													System.out.println(tokens_date[1] + "      " + tokens_date[1].length());
													System.out.println(tokens_date[2] + "      " + tokens_date[2].length());
												}
												/*
												if( (!tokens_date[0].length()==1 || !tokens_date[0].length()==2)
													(!tokens_date[1].length()==1 || !tokens_date[1].length()==2)
													(!tokens_date[2].length()==4)
													)
												*/
												
												
												try {
													  MM = Integer.parseInt(tokens_date[0]);
													  DD = Integer.parseInt(tokens_date[1]);
													  YY = Integer.parseInt(tokens_date[2]);
												} catch (NumberFormatException e) {
													  //No problem this time, but still it is good practice to care about exceptions.
													  //Never trust user input :)
													  //Do something! Anything to handle the exception.
													  my_exception("UPDATE: ERROR INVALID_DATE");
													  ok=2; break;
												}
												
												
												//general check
												if( MM<=0 || DD<=0 || YY<=0 ) {
													my_exception("UPDATE: ERROR INVALID_DATE");
													ok=2; break;
												}
			
												//MM
												if ( MM>12 ) {
													my_exception("UPDATE: ERROR INVALID_DATE");
													ok=2; break;
												}
												
												//DD
												if ( (MM/2)==0 ) {
													if( DD>30) {
														my_exception("UPDATE: ERROR INVALID_DATE");
														ok=2; break;
													}
												}
												else {
													if( DD>31) {
														my_exception("UPDATE: ERROR INVALID_DATE");
														ok=2; break;
													}
												}
												
												//Feb
												if ( (MM)==2 ) {
													if( (YY/400)==0 || ((YY/100)!=0&&(YY/4)==0)  ) {
														//both 4 & 100
														if( DD>29 ) {
															my_exception("UPDATE: ERROR INVALID_DATE");
															ok=2;
															break;
														}
													}
													else {
														if( DD>28 ) {
															my_exception("UPDATE: ERROR INVALID_DATE");
															ok=2;
															break;
														}
													}
												}
												
												
												//Passed all checks
												old.C(tokens_t[3]);
												ok=1;
											} // update_type == 4 end // seen date
											out="UPDATE: OK " + tokens_arg[1] + " " + tokens_arg[2]; //print original with double quotes	
											ok=1;
    										if(JDBG) { System.out.println(old.Movie +  " " +  old.Direct +  " " +  old.Date +  " " +  old.Seen); }
										}
										else {
											my_exception("UPDATE: ERROR WRONG_ARGUMENT_COUNT");
											ok=2; break;
										}
									//out="";
									//out = old.Movie + "," + old.Direct + "," + old.Date;
									//writeFile1(out, true);
									}
								}// all done
								
								if(ok==1) {
									System.out.println(out);
									// <title> <director> <watchedstate>
									// <title> <director> <releasedata>
									// <oldtitle> <old director> <newtitle> <newdirector>
									writeFile1(out, true); //++ <title> <director>
								}
								else if (ok==2) { 
									; //has done
								}
								else { //ok==0
									out="UPDATE: ERROR FILM_NOT_FOUND";
									System.out.println(out);
									// <title> <director> <watchedstate>
									// <title> <director> <releasedata>
									// <oldtitle> <old director> <newtitle> <newdirector>
									writeFile1(out, true); //++ <title> <director>
								}
								break;
								
						case "STORE": 
								//TODO: check file name
								if(tokens.length!=2) { 
									my_exception("STORE: ERROR WRONG_ARGUMENT_COUNT");
									break;
								}

								//PrintWriter writer0 = new PrintWriter(tokens[1], "UTF-8");
								//writer.println("The first line");
								//writer.println("The second line");
								//writer0.close();
								
								File fout2 = new File(tokens[1]); //TODO
								/*
								if (!fout2.exists()) {
									fout2.createNewFile();
								} else {
									FileOutputStream writer = new FileOutputStream(tokens[1]);
									writer.write(("").getBytes());
									writer.close();
								}
								*/
								
								FileOutputStream fos2 = new FileOutputStream(fout2);
								/*
								printwriter.print.line
								bufferwriter
								filewriter
								*/
								
								BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(fos2));
							 
							 
							 
								int ui = 0;
								for (ui = 0; ui <list.size(); ui++) {
									//read from StringArray
									Database get_tmp = list.get(ui);
								
								//compose
									out = "";
									out += get_tmp.Date+",";
									out += get_tmp.Movie+",";
									out += get_tmp.Direct+",";
									out += get_tmp.Seen;
								
									//write to csv
									bw2.write(out);
									bw2.newLine();
								}	
								bw2.close();

								//ui++;
								out="STORE: OK " + ui;
								System.out.println(out);
								writeFile1(out, true); //++ <num>
								break;	
						case "LOAD": 
								if(tokens.length!=2) { 
									my_exception("LOAD: ERROR WRONG_ARGUMENT_COUNT");
									break;
								}
								
								/////////TODO
								//String w_buf; 
								String fileName2 =tokens[1];
								
								File currentDir2 = new File("").getAbsoluteFile();
								//System.out.println(currentDir2);
								//String currentDir = System.getProperty("user.dir");
								//System.out.println("Current dir using System:" +currentDir);

								String line2;
								int csv_line = 0;
								int ok_file = 1;
								try {
									// use BufferedReader and apply on FileReader. process streaming apply on node streaming
									BufferedReader csv = new BufferedReader(new FileReader(currentDir2+"/"+fileName2));

									// read the first line 2
									line2 = csv.readLine();
									// if null
									while(line2!=null)
									{
										//csv one array
										String delims_comma = "[,]+"; //target: "the it   hard        concentrate";
										String[] tokens_csv = line2.split(delims_comma);
										for (int xx = 0; xx < tokens_csv.length; xx++) {
											if(JDBG) { System.out.println("LOAD" + xx + ": " + tokens_csv[xx]); }
										}
								
										//stringArray one array
										System.out.println(list.size());
										//System.out.println(get_);
										if (list.size()>csv_line) {
											Database get_ = list.get(csv_line);
											get_.A(tokens_csv[1]);
											get_.B(tokens_csv[2]);
											get_.C(tokens_csv[0]);
											get_.D(tokens_csv[3]);
										}
										else { // create new
											Database added2 = new Database();
											//get_tmp1.Movie = get_tmp.Movie.replace("\"","");
											//get_tmp1.Direct = get_tmp.Direct.replace("\"","");
											//get_tmp1.Date = get_tmp.Date.replace("\"","");
											
											added2.DataBase(tokens_csv[1],tokens_csv[2],
															tokens_csv[0],tokens_csv[3]); 
											list.add(added2);
											if(JDBG) {
												Database get1 = list.get(list.size()-1);
												System.out.println("ADD1:" + get1.Movie);
												System.out.println("ADD2:" + get1.Direct);
												System.out.println("ADD3:" + get1.Date);
												System.out.println("ADD4:" + get1.Seen);
											}	

										}

										// read the next line
										csv_line++;
										line2 = csv.readLine();
									} //file ends 
									
									//close the "pipe"
									csv.close();
								} catch(IOException iox) {
									System.out.println("LOAD: ERROR FILE_NOT_FOUND");
									ok_file=0;
									writeFile1("LOAD: ERROR FILE_NOT_FOUND", true); //++ <num>
									//System.out.println("LOAD: ERROR FILE_NOT_FOUND" + fileName2);
								}

								if(ok_file==0) { break; }
									
								out="LOAD: OK " + csv_line;
								System.out.println(out);
								writeFile1(out, true); //++ <num>
								break;	

						case "SEARCH": 
								if(JDBG) { System.out.println("SEARCH:" + search_arg); }
								System.out.println("SEARCH:" + search_arg);
								if(tokens.length!=2 && tokens.length!=3) { 
									my_exception("SEARCH: ERROR WRONG_ARGUMENT_COUNT");
									break;
								}
								
								
								
								//TODO:if 3
								if (tokens.length==3) {
									// with "" or without 
									////////////////////////////////////////////////
									int count_Dquote = count_(line);
									System.out.println(count_Dquote);
									
									if(count_Dquote==0) {	
										//bad: without ""
										my_exception("SEARCH: ERROR WRONG_ARGUMENT_COUNT");
										break;
									}
									else if(count_Dquote==2) {
										//good : with ""
										;
									}
								} //end (=3)
								
								//SEARCH
								int match_cnt=0;
								String out_string="";
								cnt=0;
								String search_arg_ = search_arg.replace("\"","");
								for (int j3=0 ; j3<list.size() ; j3++) { 
									if(JDBG) { System.out.println(j3+":"); }
									Database get_tmp = list.get(j3);
									
									//System.out.println("SHOW1" + get_tmp.Movie);
									//System.out.println("SHOW2" + get_tmp.Direct);
									//System.out.println("SHOW3" + get_tmp.Date);
									//System.out.println("SHOW4" + get_tmp.Seen);

									if ( get_tmp.Movie.equals(search_arg_) 
									  || get_tmp.Direct.equals(search_arg_) ) {
										match_cnt++;
										out_string = out_string + get_tmp.Movie + "," + get_tmp.Direct 
													+ "," + get_tmp.Date + "," + get_tmp.Seen + "\n";
									}
								}
								
								out="SEARCH: OK " + match_cnt ; 
								System.out.println(out);
								writeFile1(out, true);

								System.out.println(out_string);
								writeFile1(out_string, true);
								//<title1>,<director1>,<releasedate1>,<watched1>
								break;
						case "": 	
								break;
						default:
								my_exception(tokens[0] + ": ERROR UNKNOWN_COMMAND");
								break;
					}
					// output to out.txt 
				}
				// read the next line
				line = in.readLine();
			} //file ends 
			//close the "pipe"
			in.close();
		} catch(IOException iox) {
			my_exception("FILE_NOT_FOUND: ERROR cannot open file " + fileName);
			//System.out.println("FILE_NOT_FOUND: ERROR cannot open file " + fileName);
			//writeFile1("FILE_NOT_FOUND: ERROR cannot open file " + fileName, true);
		}
	}
		
	public static void my_exception(String str_excep) throws IOException {
		System.out.println(str_excep);
		writeFile1(str_excep, true);
	}
	
	public static void writeFile1(String str, boolean overwrite) throws IOException {
		File fout = new File("out.txt");
		FileOutputStream fos = new FileOutputStream(fout, overwrite);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	 
		if(overwrite==true) {
		//for (int i = 0; i < 10; i++) {
			bw.write(str);
			bw.newLine();
		//}
		}
		bw.close();
	}
	
	public static int count_(String str) {
		//String str = "abcdefabhjlecababcab";
		String str1 = "\"";
		int count = 0;
		int start = 0;
		while (str.indexOf(str1, start) >= 0 && start < str.length()) {
			count++;
			start = str.indexOf(str1, start) + str1.length();
		}
		//System.out.println("count="+count);
		return count;
	}
	
	

		
}

/*
ArrayList<String> al = new ArrayList<String>();
    al.add("a");
    al.add("b");
    //al.add("b");
    //al.add("c");
    //al.add("d");

    for (int i = 0; i < al.size(); i++) {
        if (al.get(i) == "b") {
            al.remove(i);
            i--;
        }
    }
*/
