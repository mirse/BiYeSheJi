package com.example.administrator.chatapp.Utils;

/**
 * Created by Administrator on 2018/2/6.
 */

public class pattren_KMP {
    public static int KMP(String source,String subStr)
    {
        int len1,len2;
        len1=source.length();
        len2=subStr.length();
        int i,j;
        i=j=0;
        int times=0;
        while(i<len1)
        {

            if(source.charAt(i)==subStr.charAt(j))
            {
                i++;
                j++;

            }else
            {
                if(j==0)/*这一步很重要，如果没有会进入死循环，也就是，如果主串某位与子串*/
                    i++;/*第一位不等的话，必须往后移位。*/
                j=next(subStr,j);

            }
            if(j==len2)
            {
                times++;
                j=0;

            }

        }
        return times;
    }
    static int next(String subStr,int j)
    {
        if(j==0)
            return 0;
        else {
            int next=0;
            int k=1;
            int m1;
            int m2;
            int i,n;
	     /*这一循环对应实现上面函数的第二项*/
            while(k<j)
            {
                String sub1="",sub2="";
                for(m1=0,m2=j-k;m1<k&&m2<j;m1++,m2++)
                {
                    sub1+=subStr.charAt(m1);
                    sub2+=subStr.charAt(m2);
                }

                for(i=0,n=0;i<sub1.length()&&n<sub2.length();i++,n++)
                {
                    if(sub1.charAt(i)!=sub2.charAt(n))
                        break;
                }
                if(i==sub1.length()&&n==sub2.length())
                    next=k;
                k++;
            }
            return next;
        }

    }
}
