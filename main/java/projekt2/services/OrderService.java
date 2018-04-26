package projekt2.services;

import projekt2.entities.Client;
import projekt2.entities.Item;
import projekt2.entities.Order;
import projekt2.entities.OrderItem;
import projekt2.repositories.ItemRepository;
import projekt2.repositories.OrderItemRepository;
import projekt2.repositories.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public class OrderService
{
    private OrderRepository orderRepo;
    private OrderItemRepository orderItemRepo;
    private ItemRepository itemRepo;

    public OrderService(OrderRepository orderRepo, OrderItemRepository orderItemRepo, ItemRepository itemRepo)
    {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.itemRepo = itemRepo;
    }

    public List<Order> getClientOrders(Client client)
    {
        if (client == null)
        {
            throw new IllegalArgumentException("client");
        }

        return orderRepo.getByClientId(client.getId());
    }

    public boolean addItemToOrder(Item item, Order order)
    {
        if (item == null || order == null)
        {
            return false;
        }

        boolean alreadyTaken = orderItemRepo.getByItemId(item.getId()) != null;
        if (alreadyTaken)
        {
            return false;
        }

        OrderItem orderItem = new OrderItem(order.getId(), item.getId());
        return orderItemRepo.add(orderItem);
    }
}
