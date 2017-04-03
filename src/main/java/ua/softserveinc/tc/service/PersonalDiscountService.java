package ua.softserveinc.tc.service;

import java.util.List;
import ua.softserveinc.tc.dto.PersonalDiscountDTO;
import ua.softserveinc.tc.entity.PersonalDiscount;
import ua.softserveinc.tc.entity.pagination.DataTableOutput;
import ua.softserveinc.tc.entity.pagination.SortingPagination;

public interface PersonalDiscountService extends BaseService<PersonalDiscount> {

  List<PersonalDiscountDTO> findAllPersonalDiscounts();

  void addNewPersonalDiscount(PersonalDiscountDTO personalDiscountDTO);

  void updatePersonalDiscountById(PersonalDiscountDTO personalDiscountDTO);

  PersonalDiscountDTO findPersonalDiscountById(Long id);

  List<PersonalDiscountDTO> findPersonalDiscountByUserId(Long id);

  DataTableOutput<PersonalDiscountDTO> paginateDayDiscount(SortingPagination sortPaginate);

}
