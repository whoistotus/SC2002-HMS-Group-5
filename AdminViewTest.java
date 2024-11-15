import java.io.InputStream;
import java.util.Scanner;

public class AdminViewTest {
    private static AdminView adminView;
    private static final InputStream systemIn = System.in;

    public static void main(String[] args) {
        testShowMenu();
        System.setIn(systemIn); // Reset system input
    }

    private static void testShowMenu() {
        Scanner scanner = new Scanner(System.in);
        InventoryController inventoryController = new InventoryController();
        adminView = new AdminView(inventoryController);
        
        System.out.println("Testing AdminView showMenu...");
        adminView.showMenu();
        System.out.println("Menu test completed");
    }
}