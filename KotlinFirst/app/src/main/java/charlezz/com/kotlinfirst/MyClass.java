package charlezz.com.kotlinfirst;

/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by Charles on 10/10/2017.
 */

public class MyClass {
	private static final MyClass ourInstance = new MyClass();

	static MyClass getInstance() {
		return ourInstance;
	}

	private MyClass() {
	}

	public String getHello() {
		return "Hello";
	}
}
