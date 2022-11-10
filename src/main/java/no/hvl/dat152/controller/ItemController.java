package no.hvl.dat152.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import no.hvl.dat152.model.Item;
import no.hvl.dat152.repositories.ItemDAOMemorySingleton;

@Controller
public class ItemController{

	@RequestMapping(value = "/", method = RequestMethod.GET)
    public String viewShoppingDefault() {
		return "index";
    }
	
	@RequestMapping(value = "/viewitems", method = RequestMethod.GET)
    public String viewShoppingList(Model model) {
		
		final List<Item> items = ItemDAOMemorySingleton.getInstance().findAllItems();
		model.addAttribute("items", items);
		
		return "shoppinglist";
        
    }
    
	@RequestMapping(value = "/viewitem/{id}", method = RequestMethod.GET)
    protected String viewItem(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {

        final Item item = ItemDAOMemorySingleton.getInstance().findItem(id);
        
        if(item == null){
            redirectAttributes.addFlashAttribute("errormsg", "Error EQUISDE");
            return "redirect:/viewitems";
        }
        
        model.addAttribute("item", item);

        return "viewitem";
    }
	
	@RequestMapping(value = "/createitem", method = RequestMethod.GET)
    protected String createItem(Model model) {

        final String id = ItemDAOMemorySingleton.getInstance().getNextId();
        model.addAttribute("id",id);
        
        return "createitem";
    }
	
	@RequestMapping(value = "/createitem", method = RequestMethod.POST)
    protected String createItem(@RequestParam String id, @RequestParam String name, 
    		                    @RequestParam Double price, @RequestParam String description, 
    		                    Model model) {

		final Item newItem = new Item(id, name, price, description);
		ItemDAOMemorySingleton.getInstance().createItem(newItem); 
        
        return "redirect:viewitems";
    }

    @RequestMapping(value = "/deleteitem/{id}", method = RequestMethod.GET)
    protected String deleteItem(@PathVariable String id, Model model) {
        Optional<Item> item = Optional.of(ItemDAOMemorySingleton.getInstance().findItem(id));

        model.addAttribute(item.get());

        return "deleteitem";
    }

    @RequestMapping(value = "/deleteitemsave/{id}", method = RequestMethod.GET)
    protected String deleteItemSave(@PathVariable String id, Model model) {
        ItemDAOMemorySingleton.getInstance().deleteItem(id);

        return "redirect:/viewitems";
    }

    @RequestMapping(value = "/updateitem/{id}", method = RequestMethod.GET)
    protected String updateItem(@PathVariable String id, Model model) {
        Optional<Item> item = Optional.of(ItemDAOMemorySingleton.getInstance().findItem(id));

        if (item.isEmpty()) {
            return "redirect:/viewitems";
        }

        model.addAttribute(item.get());

        return "createitem";
    }

}
