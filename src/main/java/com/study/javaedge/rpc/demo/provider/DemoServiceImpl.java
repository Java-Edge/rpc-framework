package com.study.javaedge.rpc.demo.provider;

import java.awt.Point;

import com.study.javaedge.rpc.demo.DemoService;

public class DemoServiceImpl implements DemoService {
	public String sayHello(String name) {
		return "Hello " + name;
	}

	@Override
	public Point multiPoint(Point p, int multi) {
		p.x = p.x * multi;
		p.y = p.y * multi;
		return p;
	}
}
