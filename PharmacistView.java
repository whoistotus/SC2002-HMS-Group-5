public class PharmacistView {
	
    public void viewAppointmentOutcomeRecord(String apppointmentID) {
        AppointmentOutcomeRecord.viewRecord(apppointmentID);
    }
    
    public void displayInventory() {
    	InventoryView.displayInventory();
    }

}