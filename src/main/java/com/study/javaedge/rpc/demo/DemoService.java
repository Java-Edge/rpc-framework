package com.study.javaedge.rpc.demo;

import java.awt.Point;

public interface DemoService {
	String sayHello(String name);

	Point multiPoint(Point p, int multi);
}
