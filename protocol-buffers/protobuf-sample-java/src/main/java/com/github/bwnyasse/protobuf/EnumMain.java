package com.github.bwnyasse.protobuf;

import example.enumerations.EnumExample;
import example.enumerations.EnumExample.EnumMessage;

public class EnumMain
{

	public static void main(String[] args)
	{
		EnumMessage.Builder builder = EnumMessage.newBuilder();

		builder.setId(345);

		// example with Enums
		builder.setDayOfTheWeek(EnumExample.DayOfTheWeek.FRIDAY);

		EnumMessage message = builder.build();

		System.out.println(message);
	}
}
