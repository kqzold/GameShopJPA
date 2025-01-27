package com.gam_shop.gameshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.gam_shop.gameshop.interfaces.ProductHelper;
import com.gam_shop.gameshop.interfaces.UserHelper;
import com.gam_shop.gameshop.interfaces.OrderHelper;
import com.gam_shop.gameshop.interfaces.Input;
import org.springframework.boot.CommandLineRunner;




@SpringBootApplication
public class GameShopApplication implements CommandLineRunner {

	private final Input input;
	private final UserHelper userHelper;
	private final ProductHelper productHelper;
	private final OrderHelper orderHelper;

	public GameShopApplication(Input input, UserHelper userHelper, ProductHelper productHelper, OrderHelper orderHelper) {
		this.input = input;
		this.userHelper = userHelper;
		this.productHelper = productHelper;
		this.orderHelper = orderHelper;
	}

	@Override
	public void run(String... args) {
		System.out.println("------ Магазин цветов ------");
		boolean repeat = true;

		while (repeat) {
			printTaskList();

			System.out.print("Введите номер задачи: ");
			int task = input.getInt();

			switch (task) {
				case 0:
					repeat = false;
					break;
				case 1:
					productHelper.create();
					break;
				case 2:
					productHelper.printList(productHelper.getAllEntities());
					break;
				case 3:
					productHelper.updateProduct();
					break;
				case 4:
					userHelper.create();
					break;
				case 5:
					userHelper.listUsers();
					break;
				case 6:
					userHelper.updateUser();
					break;
				case 7:
					orderHelper.processPurchase();
					break;
				case 8:
					orderHelper.calculateIncome();
					break;
				default:
					System.out.println("Выберите задачу из списка!");
			}
		}
		System.out.println("До свидания :)");
	}

	private void printTaskList() {
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
	}

	public static void main(String[] args) {
		SpringApplication.run(GameShopApplication.class, args);
	}
}