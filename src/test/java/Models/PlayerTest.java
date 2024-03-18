package Models;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
/**
 * This class tests the sequential execution of orders.
 */
public class PlayerTest {
    /**
     * List of players
     */
    List<Player> d_playersList=new ArrayList<Player>();
    /**
     * Initial method to add players
     */
    @Before
    public void init(){
        d_playersList.add(new Player("Devanshu"));
        d_playersList.add(new Player("John"));
    }
//    /**
//     * This method tests the fetching of the next order.
//     */
//    @Test
//    public void testNextExecution(){
//        Order l_order=new Order();
//        l_order.setD_orderAction("deploy");
//        l_order.setD_targetCountryName("France");
//        l_order.setD_numberOfArmiesToDeploy(5);
//
//        Order l_newOrder=new Order();
//        l_newOrder.setD_orderAction("deploy");
//        l_newOrder.setD_targetCountryName("Spain");
//        l_newOrder.setD_numberOfArmiesToDeploy(2);
//
//        List<Order> l_orderlist = new ArrayList<>();
//		l_orderlist.add(l_order);
//		l_orderlist.add(l_newOrder);
//
//		d_playersList.get(0).setIssuedOrders(l_orderlist);
//		Order l_currOrder = d_playersList.get(0).next_order();
//
//		assertEquals(l_currOrder, l_order);
//		assertEquals(d_playersList.get(0).getIssuedOrders().size(), 1);
//    }
}
