package main;

import java.util.ArrayList;
import java.util.List;

import entities.Event;
import entities.EventStatus;
import entities.EventType;
import entities.Module;
import entities.Room;
import entities.Staff;
import entities.Student;
import entities.StudentInEvent;

public class StartUp
{

	public static void main(String[] args)
	{
		Module module = new Module("SET10101", "Software Architecture");
		
		Room room = new Room("D2", 45);
		
		Staff john = new Staff("John Smith");
		Staff mary = new Staff("Mary Anderson");
		
		// STUDENTS
		Student alan = new Student("40000100");
		Student jess = new Student("40000101");
		Student greg = new Student("40000102");
		Student zander = new Student("40000103");
		Student nicola = new Student("40000104");
		Student tony = new Student("40000105");
		
		List<Student> students = new ArrayList<Student>();
		students.add(alan);
		students.add(nicola);
		students.add(tony);
		students.add(jess);
		students.add(greg);
		students.add(zander);
		
		// EVENT
		Event e1 = new Event(1, EventType.LECTURE, "01");
		e1.setModule(module);
		e1.setRoom(room);
		e1.getStaff().add(john);
		
		Event e2 = new Event(2, EventType.PRACTICAL, "01");
		e2.setModule(module);
		e2.setRoom(room);
		e2.getStaff().add(mary);
		
		Event e3 = new Event(3, EventType.PRACTICAL, "02");
		e3.setModule(module);
		e3.setRoom(room);
		e3.getStaff().add(john);
		e3.getStaff().add(mary);
		
		Event e4 = new Event(4, EventType.TUTORIAL, "01");
		e4.setModule(module);
		e4.setRoom(room);
		e4.getStaff().add(mary);
		
		Event e5 = new Event(5, EventType.TUTORIAL, "02");
		e5.setModule(module);
		e5.setRoom(room);
		e5.getStaff().add(john);
		
		// MODULE/ROOM
		List<Event> events = new ArrayList<Event>();
		events.add(e1);
		events.add(e2);
		events.add(e3);
		events.add(e4);
		events.add(e5);
		
		module.setEvents(events);
		room.setEvents(events);
		
		//STAFF
		john.getEvents().add(e1);
		john.getEvents().add(e3);
		john.getEvents().add(e5);
		
		mary.getEvents().add(e2);
		mary.getEvents().add(e3);
		mary.getEvents().add(e4);
		
		// STUDENT IN EVENTS
		StudentInEvent a1 = new StudentInEvent(EventStatus.RED);
		a1.setEvent(e1);
		a1.setStudent(alan);
		e1.getStudents().add(a1);
		alan.getEvents().add(a1);
		
		StudentInEvent a2 = new StudentInEvent(EventStatus.YELLOW);
		a2.setEvent(e2);
		a2.setStudent(alan);
		e2.getStudents().add(a2);
		alan.getEvents().add(a2);
		
		StudentInEvent j1 = new StudentInEvent(EventStatus.GREEN);
		j1.setEvent(e3);
		j1.setStudent(jess);
		e3.getStudents().add(j1);
		jess.getEvents().add(j1);
		
		StudentInEvent j2 = new StudentInEvent(EventStatus.WHITE);
		j2.setEvent(e4);
		j2.setStudent(jess);
		e4.getStudents().add(j2);
		jess.getEvents().add(j2);
		
		StudentInEvent g1 = new StudentInEvent(EventStatus.RED);
		g1.setEvent(e5);
		g1.setStudent(greg);
		e5.getStudents().add(g1);
		greg.getEvents().add(g1);
		
		StudentInEvent g2 = new StudentInEvent(EventStatus.YELLOW);
		g2.setEvent(e1);
		g2.setStudent(greg);
		e1.getStudents().add(g2);
		greg.getEvents().add(g2);
		
		StudentInEvent z1 = new StudentInEvent(EventStatus.GREEN);
		z1.setEvent(e2);
		z1.setStudent(zander);
		e2.getStudents().add(z1);
		zander.getEvents().add(z1);
		
		StudentInEvent z2 = new StudentInEvent(EventStatus.WHITE);
		z2.setEvent(e3);
		z2.setStudent(zander);
		e3.getStudents().add(z2);
		zander.getEvents().add(z2);
		
		StudentInEvent n1 = new StudentInEvent(EventStatus.RED);
		n1.setEvent(e4);
		n1.setStudent(nicola);
		e4.getStudents().add(n1);
		nicola.getEvents().add(n1);
		
		StudentInEvent n2 = new StudentInEvent(EventStatus.YELLOW);
		n2.setEvent(e5);
		n2.setStudent(nicola);
		e5.getStudents().add(n2);
		nicola.getEvents().add(n2);
		
		StudentInEvent t1 = new StudentInEvent(EventStatus.GREEN);
		t1.setEvent(e1);
		t1.setStudent(tony);
		e1.getStudents().add(t1);
		tony.getEvents().add(t1);
		
		StudentInEvent t2 = new StudentInEvent(EventStatus.WHITE);
		t2.setEvent(e4);
		t2.setStudent(tony);
		e4.getStudents().add(t2);
		tony.getEvents().add(t2);
		
		// PRINTING
		System.out.println(module.toString());
		System.out.println();
		System.out.println(room.toString());
		System.out.println();
		System.out.println(john.toString());
		System.out.println();
		System.out.println(mary.toString());
		
		for(Student s : students)
		{
			System.out.println(s.toString());
			System.out.println();
		}
	}

}
