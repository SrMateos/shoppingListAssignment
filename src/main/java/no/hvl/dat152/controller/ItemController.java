package no.hvl.dat152.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import no.hvl.dat152.model.Item;
import no.hvl.dat152.service.ItemService;

@Controller
public class ItemController{

    @Autowired
    private ItemService itemService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
    public String viewShoppingDefault() {
		return "index";
    }
	
	@RequestMapping(value = "/viewitems", method = RequestMethod.GET)
    public String viewShoppingList(Model model) {
		
		final List<Item> items = itemService.getAll();
		model.addAttribute("items", items);
		
		return "shoppinglist";
        
    }
    
	@RequestMapping(value = "/viewitem/{id}", method = RequestMethod.GET)
    protected String viewItem(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {

        final Item item = itemService.getById(id).get();
        
        if(item == null){
            redirectAttributes.addFlashAttribute("errormsg", "Error, item not found");
            return "redirect:/viewitems";
        }
        
        model.addAttribute("item", item);

        return "viewitem";
    }
	
	@RequestMapping(value = "/createitem", method = RequestMethod.GET)
    protected String createItem() {

        return "createitem";
    }
	
	@RequestMapping(value = "/createitem", method = RequestMethod.POST)
    protected String createItem(@RequestParam String name,
    		                    @RequestParam Double price, @RequestParam String description) {

		final Item newItem = new Item(name, price, description);
		itemService.save(newItem); 
        
        return "redirect:viewitems";
    }

    @RequestMapping(value = "/deleteitem/{id}", method = RequestMethod.GET)
    protected String deleteItem(@PathVariable Long id, Model model) {
        Optional<Item> item = itemService.getById(id);
        model.addAttribute(item.get());

        return "deleteitem";
    }

    @RequestMapping(value = "/deleteitemsave/{id}", method = RequestMethod.GET)
    protected String deleteItemSave(@PathVariable Long id, Model model) {
        itemService.delete(id);
        return "redirect:/viewitems";
    }

    @RequestMapping(value = "/updateitem/{id}", method = RequestMethod.GET)
    protected String updateItem(@PathVariable Long id, Model model) {
        model.addAttribute("item", itemService.getById(id).get());
        return "updateitem";
    }

    @RequestMapping(value = "/updateitem/{id}", method = RequestMethod.POST)
    protected String updateItem(@PathVariable Long id, Model model, @RequestParam String name,
                                @RequestParam Double price, @RequestParam String description,
                                RedirectAttributes redirectAttributes) {
        Optional<Item> item = itemService.getById(id);

        if (item == null) {
            System.out.println("IM HERRRRRRRRREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
            redirectAttributes.addFlashAttribute("errormsg", "Error, item not found");
            return "redirect:/viewitems";
        }

        Item modifiedItem = new Item(id, name, price, description);
        itemService.update(id, modifiedItem);

        model.addAttribute(modifiedItem);

        return "redirect:/viewitems";
    }

}
