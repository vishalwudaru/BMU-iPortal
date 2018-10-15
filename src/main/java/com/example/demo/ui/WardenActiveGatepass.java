package com.example.demo.ui;


import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entities.GatepassTable;
import com.example.demo.entities.Student;
import com.example.demo.entities.User;
import com.example.demo.service.GatepassService;
import com.example.demo.service.StudentService;
//import com.example.demo.entities.Attendance;
//import com.example.demo.service.StudentService;
//import com.example.demo.ui.AttendanceView.AttendanceHandler;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@org.springframework.stereotype.Component
public class WardenActiveGatepass {
	
	@Autowired
	GatepassService service;
	
	@Autowired
	GatepassWindow gatepassWindow;
	
	@Autowired
	WardenWindow wardenWindow;
	
	@Autowired
	WardenActiveGatepassWindow activeWindow;
	
	@Autowired
	StudentService studentService;
	
	public class gatepassHandler
	{
		
		public gatepassHandler init()
		{
			return this;
		}
		
		public Component layout(User currentUser)
		{
			Student student=studentService.getStudentByParent(currentUser);
			VerticalLayout layout2=new VerticalLayout();
			layout2.setSizeFull();
			Grid<GatepassTable> grid = new Grid<>(GatepassTable.class);
			grid.setWidth("90%");
			grid.setItems(service.getallGatepassByMultipleStatus());
			grid.removeAllColumns();
			grid.addColumn("gatepassId").setCaption("Gatepass ID");
			grid.addColumn("sname").setCaption("Student Name");
			grid.addColumn("studentReason").setCaption("Student Reason");			
			grid.addColumn("requestedCheckin").setCaption("Requested CheckIn Time");
			grid.addColumn("requestedCheckout").setCaption("Requested CheckOut Time");
			grid.addColumn("status");
			grid.addColumn("studentIp").setCaption("Student IP");
			grid.addColumn("parentIp").setCaption("Parent IP");
			
			grid.asSingleSelect().addValueChangeListener(event ->{
				if(event.getValue()!=null && event.getValue().getStatus().equals("Initiated")==true)
				{
					UI.getCurrent().addWindow((Window) gatepassWindow.createComponent(event.getValue(),grid));
				}
				
			});
			
			grid.asSingleSelect().addValueChangeListener(event ->{
				if(event.getValue()!=null && (event.getValue().getStatus().equals("Parent Approved")==true 
						|| event.getValue().getStatus().equals("IP Conflict")==true) )
				{
					UI.getCurrent().addWindow((Window) activeWindow.createComponent(event.getValue(),currentUser,grid));
				}
				
			});	
			
//			grid.addColumn("requestedCheckin").setCaption("Requested Checkin");
			grid.sort("gatepassId",SortDirection.DESCENDING);
			layout2.addComponent(grid);
			return layout2;
		}
		

}
	
public Component createComponent(User user){
	return new gatepassHandler().init().layout(user);
}
}

