package grokswell.hypermerchant;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import regalowl.hyperconomy.EconomyManager;
import regalowl.hyperconomy.HyperConomy;
import regalowl.hyperconomy.HyperEconomy;
import regalowl.hyperconomy.HyperObjectAPI;
import regalowl.hyperconomy.HyperPlayer;


public class ShopStock {
	ArrayList<ArrayList<String>> pages = new ArrayList<ArrayList<String>>();
	ArrayList<String> items_in_stock = new ArrayList<String>();
	ArrayList<String> item_types_sorted = new ArrayList<String>();
	ArrayList<String> item_materials_sorted = new ArrayList<String>();
	HashMap<String,String> items_by_type = new HashMap<String,String>();
	HashMap<String,String> items_by_material = new HashMap<String,String>();
	int items_count;
	private String shopname;
	private CommandSender sender;
	public HyperConomy hc;
	public EconomyManager ecoMan;
	HyperPlayer hp;
    HyperEconomy hEcon;
	private HyperObjectAPI hoa;
	
	ShopStock(CommandSender snder, Player player, String sname, HyperMerchantPlugin hmp) {
		hc = HyperConomy.hc;
		ecoMan = hc.getEconomyManager();
		hp = ecoMan.getHyperPlayer(player);
		hEcon = hp.getHyperEconomy();
		hoa = new HyperObjectAPI();
		shopname = sname;
		sender = snder;

		try {
    		String nameshop = ecoMan.getShop(shopname).getName();
			ArrayList<String> names = hEcon.getNames();
			Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
			//out.println("shopname: "+ nameshop);
			//out.println("heCon names: "+ names);
			int i = 0;
			
			while(i < names.size()) {
				String cname = names.get(i);
				items_in_stock.add(cname);
				String t=hoa.getType(cname, "default").name();
				if (t.toLowerCase().equals("item")) {
					item_types_sorted.add(String.valueOf(hoa.getMaterial(cname, "default").toLowerCase()+cname));
					items_by_type.put(hoa.getMaterial(cname, "default").toLowerCase()+cname,cname);
					
				} else {
					item_types_sorted.add(String.valueOf("enchantment"+cname));
					items_by_type.put("enchantment"+cname, cname);
					
					
				}
				i++;
			}			
			
			Collections.sort(items_in_stock, String.CASE_INSENSITIVE_ORDER);
			Collections.sort(item_types_sorted);
		} 
		catch (Exception e) {
			sender.sendMessage("Error, cannot open shop inventory");
			out.println(e.toString());
		}
	}

	public void SortStock (Integer sort_by) {		
		int number_per_page = 45;
		int count = 0;
		int item_index=0;
		int page = 0;
		items_count  = items_in_stock.size();
		double maxpages = items_count/number_per_page;
		maxpages = Math.ceil(maxpages);
		
		while (page <= maxpages) {
			pages.add(new ArrayList<String>());
			while (count < number_per_page) {
				if (item_index < items_count) {
					if (sort_by == 0){
						String item_name = this.items_in_stock.get(item_index);
						pages.get(page).add(item_name);
					}
					else if (sort_by == 2){
						String item_name = this.hEcon.fixName(this.items_by_type.get(this.item_types_sorted.get(item_index)));
						pages.get(page).add(item_name);
					}
				}
				count++;
				item_index++;
			}
			count=0;
			page++;
		}
		
	}
}
