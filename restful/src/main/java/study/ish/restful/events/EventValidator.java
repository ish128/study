package study.ish.restful.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class EventValidator {

  public void validate(EventDto eventDto, Errors errors){

      if(eventDto.getMaxPrice()<eventDto.getBasePrice() && eventDto.getMaxPrice()!=0){
        errors.rejectValue("maxPrice", "wrongValue","maxPrice is wrong.");
        errors.rejectValue("basePrice", "wrongValue","basePrice is wrong.");
      }

      if(eventDto.getEndEventDateTime().isBefore(eventDto.getBeginEnrollmentDateTime()) ||
      eventDto.getEndEventDateTime().isBefore(eventDto.getBeginEventDateTime())||
      eventDto.getEndEventDateTime().isBefore(eventDto.getCloseEnrollmentDateTime())){
        errors.rejectValue("endEventDateTime", "wrongValue","endEventDateTime is wrong.");
      }


  }
}
