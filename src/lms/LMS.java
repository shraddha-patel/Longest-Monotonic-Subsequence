package lms;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

/**
 *
 * @author Shraddha
 */
public class LMS {
    public int store[], maximum[];
    public static void main(String[] args) throws FileNotFoundException, IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(new FileReader("test-200-20.txt"));
        tokenizer.slashSlashComments(true);
        tokenizer.eolIsSignificant(false);
        tokenizer.nextToken();
        int n = (int)tokenizer.nval;
        tokenizer.nextToken();
        int k = (int)tokenizer.nval;
        int A[] = new int[n];
        int W[] = new int[n];
        for(int i=0;i<n;i++){
            tokenizer.nextToken();
            if (tokenizer.ttype == StreamTokenizer.TT_NUMBER){
                A[i] = (int)tokenizer.nval;
                tokenizer.nextToken();
                W[i] = (int)tokenizer.nval;
            }
        }  
        LMS lms = new LMS();
        lms.LMS(A,n,W);
        lms.LMS_Penalty(A, W, k);
    }
    public void LMS(int A[], int n, int W[]){
       int length[];
       length = new int[n];
       store = new int[n];
       for(int i=0;i<n;i++){
           store[i] = -1;
       }
       maximum = new int[n];
       maximum[0] = W[0];
       int max, max_length, end = 0;
       for(int i=0;i<n;i++){
           max = 0;
           max_length = 0;
           for(int j = 0;j<i;j++ ){
                if(A[i]>A[j]){
                    if(maximum[j] > max){
                        max_length = length[j];
                        max = maximum[j];
                        store[i] = j ;
                    }
                }
           }
           length[i] = max_length+1;
           maximum[i] = max + W[i];
       }
       
       max_length = 0;
       for (int i = 0; i<n;i++){
            if(max_length < length[i]){
                   max_length = length[i];
            }
        }
  
        max = 0;
        for (int i = 0; i<n;i++){
            if(max < maximum[i]){
                   max = maximum[i];
                   end = i;
                   max_length = length[end];
            }
        }      
        
        System.out.print("Output for part a:");
        System.out.print("\n");
        System.out.print(" "+max);
   
        int[] index;
        int i = 0;
        index = new int[max_length];
        index[i] = end;
        while(store[end]>0){
            i = i+1;
            index[i] = store[end];
            end = store[end];
        }  
        
        System.out.print("\n");
        for(i=index.length-1;i>=0;i--){
            System.out.print(" "+ +(index[i]+1));
        }
        System.out.print("\n");
        for(i=index.length-1;i>=0;i--){
             System.out.print(" "+ +A[index[i]]);
        }   
        System.out.println("\n");
    }
    
    public void LMS_Penalty(int A[], int W[], int k){
       int length[], track[][], weight[][], violation[][];
       int n = A.length;
       
       length = new int[n];
       track = new int[n][k+1];
       violation = new int[n][k+1];
       weight = new int[n][k+1];
       weight[0][0] = W[0];
       int max, penalty, max_length, end = 0 , column = 0, value;
       
       for(int i=0;i<n;i++){
           for(int j=0;j<k+1;j++){
               track[i][j] = -1;
               violation[i][j] = -1;
           }
       }
        
       for(int i=0;i<n;i++){
            weight[i][0] = maximum[i];
            track[i][0] = store[i];
            violation[i][0] = 0;
        }
              
       for(int v = 1;v<=k;v++){
            for(int i=0;i<n;i++){
                max = 0;
                max_length = 0;
                for(int j = 0;j<i;j++ ){
                    if(A[i]<=A[j]){
                        if(i!=j){
                            penalty = (A[j]-A[i])*W[i];
                            value = weight[j][v-1]  - penalty + W[i];

                            if(max==0){
                                weight[i][v] = value;
                                max = value;
                            }
                            
                            if( value<max){
                                weight[i][v] = max;
                            }
                            else{
                                max = value;
                                max_length = length[j];
                                weight[i][v] = max;
                                track[i][v] = j;
                                violation[i][v] = v-1;
                            }
                        }
                    }
                    else{
                        if(weight[j][v] + W[i]  > max ){
                            max_length = length[j];
                            max = weight[j][v] + W[i];
                            weight[i][v] = max  ;
                            track[i][v] = j ;
                            violation[i][v] = v;
                        }
                    } 

                }
                length[i] = max_length+1;
            }
       }
       max_length = 0;
       for (int i = 0; i<n;i++){
            if(max_length < length[i]){
                   max_length = length[i];
            }
        }
        
        max = 0;
        for (int i = 0; i<n;i++){
            for(int j=0;j<=k;j++){
                if(max < weight[i][j]){
                    max = weight[i][j];
                    end = i;
                    column = j;
                    max_length = length[end];
                }
            }       
        }
        System.out.print("Output for part b:");
        System.out.print("\n");
        System.out.print(" "+max);
        
        int index[], i = 0, temp;
        index = new int[n];
        index[i] = end;
        while(track[end][column]>=0){
            i = i+1;
            index[i] = track[end][column];
            temp = column;
            column = violation[end][column];
            end = track[end][temp];
        }    
        
        int total_length = i;
        System.out.print("\n");
        
        for(i=total_length;i>=0;i--){
            System.out.print(" "+ +(index[i]+1));
        }
        System.out.print("\n");
        for(i=total_length;i>=0;i--){
             System.out.print(" "+ +A[index[i]]);
        } 
        System.out.print("\n");
    }
}

