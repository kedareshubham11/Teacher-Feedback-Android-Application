package com.prathameshmore.feedbackapp;

public class FeedbackData {

    private String subject;
    private String roll_no;

}

/*
 try {
         String x= String.valueOf(i);
         sheet.addCell(new Label(0, i,x));
         String roll_no=dataSnapshot.getKey();
         sheet.addCell(new Label(1, i, roll_no));
         for(DataSnapshot ds:dataSnapshot.getChildren()) {
         String subject=ds.getKey();
         sheet.addCell(new Label(2, i, subject));
         int c=3;
         for (DataSnapshot dds : ds.getChildren()) {

         String Qns=dds.getValue().toString();
         sheet.addCell(new Label(c, i, Qns));
         c=c+1;


         }
         }


         } catch (WriteException e) {
         e.printStackTrace();
         }
         i=i+1;
         Toast.makeText(getApplicationContext(),"All Done 2",Toast.LENGTH_SHORT).show();
*/
