package study.ish.restful.index;

import org.junit.Test;
import study.ish.restful.common.BaseControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IndexControllerTest extends BaseControllerTest {

  @Test
  public void index() throws Exception {
    mockMvc.perform(get("/api"))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("_links.events").exists());
  }
}
