package com.example.parentcontrolservice.cucumber.steps;

import com.example.parentcontrolservice.ParentalControlServiceAT;
import com.example.parentcontrolservice.cucumber.World;
import com.example.parentcontrolservice.cucumber.model.TestableParentalControlLevel;
import cucumber.api.java8.En;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by raf on 6/07/18.
 */
@WebMvcTest(ParentalControlServiceAT.class)
@ContextConfiguration(loader = SpringBootContextLoader.class, classes = ParentalControlServiceAT.class)
public final class ParentalControllerSteps implements En{

  // TODO encapsulate in world
  @Autowired
  MockMvc mockMvc;

  // TODO is not RESTful - change to either RPC style or provide filters of movie and preference
  private static final String SERVICE_REQUEST_TEMPLATE = "/parentalcontrol/movie/%s/preference/%s";

  public ParentalControllerSteps (@Autowired World world) {
    Given("mother has a parental control preference setting of (.+)", (String preferredLevel) -> {
      TestableParentalControlLevel preferenceLevel = TestableParentalControlLevel.lookupByRating.get( preferredLevel );
      //TODO handle NPE
      world.setParentalLevelPreference(preferenceLevel);
    });

    Given("Jaws has a parental control level of (.+)", (String parentalControlLevel) -> {
      TestableParentalControlLevel movieLevel = TestableParentalControlLevel.lookupByRating.get(parentalControlLevel);
      //TODO handle NPE
      world.setMovieParentalLevel(movieLevel);
    });

    When("mother attempts to watch (\\w+)", (String movie) -> {
      TestableParentalControlLevel preferenceLevel = world.getParentalLevelPreference();
      String url = String.format(SERVICE_REQUEST_TEMPLATE, movie, preferenceLevel.getParentalControlLevel());
      ResultActions response = mockMvc.perform(get(url)).andExpect(status().isOk());
      //world.setResponse(response);
    });

    Then("she will not be permitted to watch it", () -> {
          //world.getResponse().andExpect(jsonPath("$.viewable", is(false)));
    });
  };
}
