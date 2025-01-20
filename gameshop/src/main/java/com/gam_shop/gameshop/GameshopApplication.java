package com.gam_shop.gameshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.gam_shop.gameshop.entity.Product;
import com.gam_shop.gameshop.entity.User;
import com.gam_shop.gameshop.services.ProductService;
import com.gam_shop.gameshop.services.UserService;
import com.gam_shop.gameshop.services.OrderService;
import com.gam_shop.gameshop.interfaces.Input;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDate;



@SpringBootApplication
public class GameshopApplication implements CommandLineRunner {

	private final Input input;
	private final ProductService productService;
	private final UserService userService;
	private final OrderService orderService;

	public GameshopApplication(Input input, ProductService productService, UserService userService, OrderService orderService) {
		this.input = input;
		this.productService = productService;
		this.userService = userService;
		this.orderService = orderService;
	}

	@Override
	public void run(String... args) {
		System.out.println("Добро пожаловать в магазин!");
		boolean repeat = true;

		while (repeat) {
			System.out.println("Список задач:");
			System.out.println("0. Выйти из программы");
			System.out.println("1. Добавить продукт");
			System.out.println("2. Список продуктов");
			System.out.println("3. Изменить атрибуты продукта");
			System.out.println("4. Добавить покупателя");
			System.out.println("5. Список покупателей");
			System.out.println("6. Изменить атрибуты покупателя");
			System.out.println("7. Купить товар");
			System.out.println("8. Посмотреть доход магазина");

			System.out.print("Введите номер задачи: ");
			int task = input.getInt();

			switch (task) {
				case 0:
					repeat = false;
					break;
				case 1:
					System.out.println("Введите название продукта:");
					String name = input.getString();
					System.out.println("Введите цену продукта:");
					double price = input.getDouble();
					System.out.println("Введите количество продукта:");
					int purchaseQuantity = input.getInt();

					Product newProduct = new Product(name, price, purchaseQuantity);
					productService.addProduct(newProduct);
					System.out.println("Продукт добавлен.");
					break;
				case 2:
					productService.getAllProducts().forEach(product ->
							System.out.printf("ID: %d, Название: %s, Цена: %.2f, Количество: %d%n",
									product.getId(), product.getName(), product.getPrice(), product.getQuantity()));
					break;
				case 3:
					System.out.println("Введите ID продукта для обновления:");
					Long updateProductId = (long) input.getInt();
					System.out.println("Введите новое название продукта:");
					String newName = input.getString();
					System.out.println("Введите новую цену продукта:");
					double newPrice = input.getDouble();
					System.out.println("Введите новое количество продукта:");
					int newQuantity = input.getInt();

					Product updatedProduct = new Product(newName, newPrice, newQuantity);
					try {
						productService.updateProduct(updateProductId, updatedProduct);
						System.out.println("Продукт обновлен.");
					} catch (RuntimeException e) {
						System.out.println(e.getMessage());
					}
					break;
				case 4:
					System.out.println("Введите имя покупателя:");
					String username = input.getString();
					System.out.println("Введите пароль покупателя:");
					String password = input.getString();
					System.out.println("Введите баланс покупателя:");
					double balance = input.getDouble();

					User newUser = new User(username, password, balance);
					userService.addUser(newUser);
					System.out.println("Покупатель добавлен.");
					break;
				case 5:
					userService.getAllUsers().forEach(user ->
							System.out.printf("ID: %d, Имя: %s, Баланс: %.2f%n",
									user.getId(), user.getUsername(), user.getBalance()));
					break;
				case 6:
					System.out.println("Введите ID покупателя для обновления:");
					Long userId = (long) input.getInt();
					System.out.println("Введите новое имя покупателя:");
					String newUsername = input.getString();
					System.out.println("Введите новый пароль покупателя:");
					String newPassword = input.getString();
					System.out.println("Введите новый баланс покупателя:");
					double newBalance = input.getDouble();

					User updatedUser = new User(newUsername, newPassword, newBalance);
					try {
						userService.updateUser(userId, updatedUser);
						System.out.println("Покупатель обновлен.");
					} catch (RuntimeException e) {
						System.out.println(e.getMessage());
					}
					break;
				case 7:
					System.out.println("Введите ID покупателя:");
					Long buyerId = (long) input.getInt();
					System.out.println("Введите ID продукта:");
					Long productId = (long) input.getInt();
					System.out.println("Введите количество:");
					int quantity = input.getInt();

					try {
						orderService.processPurchase(buyerId, productId, quantity);
						System.out.println("Покупка завершена.");
					} catch (RuntimeException e) {
						System.out.println(e.getMessage());
					}
					break;
				case 8:
					System.out.println("Выберите период:");
					System.out.println("1. День");
					System.out.println("2. Месяц");
					System.out.println("3. Год");
					int periodChoice = input.getInt();

					try {
						switch (periodChoice) {
							case 1:
								System.out.println("Введите дату (ГГГГ-ММ-ДД):");
								String dateInput = input.getString();
								LocalDate date = LocalDate.parse(dateInput);
								double dailyIncome = orderService.calculateIncomeForDay(date);
								System.out.printf("Доход за %s: %.2f%n", date, dailyIncome);
								break;
							case 2:
								System.out.println("Введите год:");
								int year = input.getInt();
								System.out.println("Введите месяц:");
								int month = input.getInt();
								double monthlyIncome = orderService.calculateIncomeForMonth(year, month);
								System.out.printf("Доход за %d-%02d: %.2f%n", year, month, monthlyIncome);
								break;
							case 3:
								System.out.println("Введите год:");
								int incomeYear = input.getInt();
								double yearlyIncome = orderService.calculateIncomeForYear(incomeYear);
								System.out.printf("Доход за %d: %.2f%n", incomeYear, yearlyIncome);
								break;
							default:
								System.out.println("Неверный выбор.");
						}
					} catch (RuntimeException e) {
						System.out.println("Ошибка: " + e.getMessage());
					}
					break;
				default:
					System.out.println("Выберите задачу из списка!");
			}
		}
		System.out.println("До свидания :)");
	}

	public static void main(String[] args) {
		SpringApplication.run(GameshopApplication.class, args);
	}
}