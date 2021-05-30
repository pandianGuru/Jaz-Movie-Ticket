package com.jaz.movie.booking;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

public class test {

    public static void main(String args[]){

        String a = "10,4,2,11";
        String b[] = a.split(",");
        Integer [] arr = new Integer [b.length];
        for(int i=0; i<b.length; i++) {
            arr[i] = Integer.parseInt(b[i]);
        }
        Collections.sort(Arrays.asList(arr));

        String.join(",",Arrays.toString(arr));
        String ch = Arrays.toString(arr).replace("]","").replace("[", "");
       System.out.println("Ss : " + ch);
    }
}
