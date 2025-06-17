package com.learn.listener;

import java.io.FileWriter;
import java.io.IOException;

import org.springframework.batch.core.SkipListener;

import com.learn.domain.OSProduct;
import com.learn.domain.Product;

public class MySkipListener implements SkipListener<Product, OSProduct> {

	@Override
	public void onSkipInRead(Throwable t) {
		// TODO Auto-generated method stub
		SkipListener.super.onSkipInRead(t);
	}

	@Override
	public void onSkipInWrite(OSProduct item, Throwable t) {
		// TODO Auto-generated method stub
		SkipListener.super.onSkipInWrite(item, t);
	}

	@Override
	public void onSkipInProcess(Product item, Throwable t) {
		System.out.println("Skipped Item : ");
		System.out.println(item);
		
		writeToFile(item.toString());
	}
	
	public void writeToFile(String data)  {
		//create  file writer object
		try {
			//true indicated file is appendable so we can append the data
			FileWriter fileWriter = new FileWriter("rejected/Product_Details_Rejected.txt",true);
			fileWriter.write(data+"\n"); // go to next line after writing the data
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
