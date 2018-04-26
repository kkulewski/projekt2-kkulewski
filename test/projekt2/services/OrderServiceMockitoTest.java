package projekt2.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import projekt2.entities.*;
import projekt2.extensions.MockitoExtension;
import projekt2.repositories.ItemRepository;
import projekt2.repositories.OrderItemRepository;
import projekt2.repositories.OrderRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class OrderServiceMockitoTest
{
    private OrderRepository orderRepo;
    private OrderItemRepository orderItemRepo;
    private ItemRepository itemRepo;
    private OrderService os;

    @BeforeEach
    void setup()
    {
        orderRepo = Mockito.mock(OrderRepository.class);
        orderItemRepo = Mockito.mock(OrderItemRepository.class);
        itemRepo = Mockito.mock(ItemRepository.class);
        os = new OrderService(orderRepo, orderItemRepo, itemRepo);
    }

    @Test
    void getClientOrdersThrowsWhenClientIsNull()
    {
        assertThatThrownBy(() -> os.getClientOrders(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("client");
    }

    @Test
    void getClientOrdersReturnsExpectedOrders()
    {
        // Arrange
        Client client = new Client(1, "John", "Doe", "johndoe@email.com");
        List<Order> expectedOrders = Arrays.asList(new Order(1, client.getId()), new Order(2, client.getId()));
        doReturn(expectedOrders).when(orderRepo).getByClientId(client.getId());

        // Act
        List<Order> result = os.getClientOrders(client);

        // Assert
        assertThat(result).containsAll(expectedOrders);
    }

    @Test
    void addItemToOrderReturnsFalseWhenItemAndOrderAreNull()
    {
        assertThat(os.addItemToOrder(null, null)).isFalse();
    }

    @Test
    void addItemToOrderReturnsFalseWhenItemIsAlreadyInAnotherOrder()
    {
        // Arrange
        Order myOrder = new Order(2, 1);
        Order anotherOrder = new Order(1, 10);
        Item item = new Item(1, "uKeyboard", 100.0);
        OrderItem orderItem = new OrderItem(anotherOrder.getId(), item.getId());
        doReturn(orderItem).when(orderItemRepo).getByItemId(item.getId());

        // Act
        boolean wasAdded = os.addItemToOrder(item, myOrder);

        // Assert
        assertThat(wasAdded).isFalse();
    }

    @Test
    void addItemToOrderReturnsTrueWhenItemWasAdded()
    {
        // Arrange
        Order myOrder = new Order(2, 1);
        Item item = new Item(1, "uKeyboard", 100.0);
        doReturn(null).when(orderItemRepo).getByItemId(item.getId());
        doReturn(true).when(orderItemRepo).add(any());

        // Act
        boolean wasAdded = os.addItemToOrder(item, myOrder);

        // Assert
        assertThat(wasAdded).isTrue();
    }
}
