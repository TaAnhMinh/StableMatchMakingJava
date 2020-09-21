import java.io.*;
import java.util.*;

public class StableMatching {
	
	public StableMatching() {
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		StableMatching a = new StableMatching();
		StableMatching b = new StableMatching();
		String[] Pair3x3 = a.ePickS("coop_e_3x3.csv", "coop_s_3x3.csv");
		String[] Pair10x10 = b.ePickS("coop_e_10x10.csv", "coop_s_10x10.csv");
		
		FileOutputStream f1 = new FileOutputStream("matches_java_3x3.csv");
		FileOutputStream f2 = new FileOutputStream("matches_java_10x10.csv");
		/* 
		 * csv file for 3x3
		 */
		PrintWriter pw = new PrintWriter(f1);
		pw.println("Student, Employer");
		int count = 0;
		int count2 = 1;
		
		for (int i = 0 ; i < Pair3x3.length/2; i++) {
			pw.println(Pair3x3[count] + "," + Pair3x3[count2]);
			count += 2;
			count2 += 2;
		}
		pw.close();
		System.out.println("file matches_java_3x3.csv has been created");
		
		/* 
		 * csv file for 10x10
		 */
		
		PrintWriter pw2 = new PrintWriter(f2);
		pw2.println("Student, Employer");
		count = 0;
		count2 = 1;
		
		for (int i = 0 ; i < Pair10x10.length/2; i++) {
			pw2.println(Pair10x10[count] + "," + Pair10x10[count2]);
			count += 2;
			count2 += 2;
		}
		pw2.close();
		System.out.println("file matches_java_10x10.csv has been created");
		
	}
	
	
	public boolean sPreferCurrentOverNew (String[][] finalList, int studentList, String newEmployer, String currentEmployer, int n) {
		for (int i = 0; i < n; i++)  
	    {  
	        if (finalList[studentList][i].equals(currentEmployer))
	            return true;  
	  
	        if (finalList[studentList][i].equals(newEmployer)) 
	        	return false;  
	    } 
	    return false; 
	}
	
	public String[] ePickS (String EFile, String SFile) {
		
		/* read the file */
		BufferedReader employeeFile = null;
		BufferedReader studentFile = null;
		int n = 0;
		int n2 = 0;
		String[][] employeeL = new String[100][100];
		String[][] studentL = new String[100][100];
		
		try {
			employeeFile = new BufferedReader (new FileReader(EFile));
			String line1;
			
			while ((line1 = employeeFile.readLine())!= null) {
		        String[] employeeList = line1.split(",");
		        for (int i = 0; i < employeeList.length; i++) 
		        	employeeL[n][i] = employeeList[i];
		        n++;
		        n2 = employeeList.length;
		    }
			
			n = 0;
			studentFile = new BufferedReader (new FileReader(SFile));
			String line2;
			while ((line2 = studentFile.readLine()) != null){
		        String[] studentList = line2.split(",");
		        for (int i = 0; i < studentList.length; i++) 
		        	studentL[n][i] = studentList[i];
		        n++;
		    }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				employeeFile.close();
				studentFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		String[][] finalList = new String[2*n][n2];	//WILL BE 6 BY 3 in 3x3 table
		for (int i = 0; i < finalList.length; i++) {	//i < 6
			for (int z = 0; z <  finalList[i].length; z++) {	//z < 3
				if (i < (finalList.length)/2) {	//if i < 3
					finalList[i][z] = employeeL[i][z];
				} else {
					finalList[i][z] = studentL[i - finalList.length/2][z];
				}
			}
		} 
		
		/* end read File and now has both lists combined into one */
		
		/*Start matching */
		
		/* Create a list of Students. This is the output array that store pair info. 
		 * Note that the Students position is from N to 2N-1
		 * The employers position is from 0 to N-1
		 * The empty string "" indicates that the student is free
		 */
		String StudentEmployer[] = new String[n];
		boolean employerFree[] = new boolean[n];  
		  
	    // Initialize all students and employers as free  
	    Arrays.fill(StudentEmployer, "Empty");  
	    int freeCount = n; 
		while (freeCount > 0)
	    {  
	        // Pick the first employer 
	        int m;  
	        for (m = 0; m < n; m++)  
	            if (employerFree[m] == false)
	                break;  
	        // One by one go to all students  
	        // according to employer's preferences.  
	        // Here m is the free employer that has not found a student to hire.  
	        for (int i = 0; i < n && employerFree[m] == false; i++) {
	        	//find the first student
	        	String student = finalList[m][i+1];
	            int p;
	            int employerIndex = 0;
	            for (p = 0 ; p < n; p++) {
	            	if (student.equals(finalList[p + n][0])) {
	            		 break;
	            	}
	            }
	            // The student of preference is free,  
	            // employer pick students (employer can change choice later. 
	            // So we can say student is hired (temp)
	            if (StudentEmployer[p] == "Empty")  
	            {  
	            	StudentEmployer[p] = finalList[m][0];  
	            	employerFree[m] = true;  
	                freeCount--;
	            }           
	            else // If student is not free  
	            {  
	                // Find current employer of student 
	                String currentEmployer = StudentEmployer[p];  
	                // If student prefers employer over the current employer,  
	                // then break the contract between employer and student and  
	                // form a new contract employer with student.  
	                if (sPreferCurrentOverNew(finalList, (p + n), finalList[m][0], currentEmployer, n) == false)  
	                {  
	                	StudentEmployer[p] = finalList[m][0];
	                	employerFree[m] = true;  
	                	for (int z = 0; z < n; z++) {
	                		if (finalList[z][0].equals(currentEmployer)) {
	                			employerIndex = z;
	                			break;
	                		}
	                	}
	                	employerFree[employerIndex] = false;  
	                }  
	            } // End of Else  
	        } // End of the for loop that goes to all employers and students
	        int allT = 0;
	        for (int j = 0; j < employerFree.length; j++) {
	        	if (employerFree[j] == true) {
	        		allT++;
	        	}
	        	if (allT == employerFree.length) {
	        		break;
	        	}
	        }
	    } // End of main while loop  
		
	  String[] finalOne = new String[2*n];
	  int count = 0;
	  int count2= 0;
	  for (int i = 0; i < finalOne.length; i++) {
		  if (i % 2 == 1) {
			  finalOne[i] = StudentEmployer[count];
			  count++;
		  } else {
			  finalOne[i] = finalList[count2+n][0];
			  count2++;
		  }
	  }
	  
	// Return solution
	return finalOne;
	
	}
}
