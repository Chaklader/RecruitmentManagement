package com.recruitment.manager.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


/**
 * Created by Chaklader on Mar, 2021
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {


    @Autowired
    private MockMvc mockMvc;

//    @MockBean
//    private CastleService castleService;

    @Autowired
    private ModelMapper modelMapper;


    @Test
    public void givenSizeOfArmy_CreateDifferentTroops() throws Exception {


//        final int ARMY_SIZE = 167;
//
//        CastleDto castleDto = new CastleDto();
//        castleDto.setTotalSize(ARMY_SIZE);
//
//        Map<TroopTypes, Integer> map = new LinkedHashMap<>();
//
//        map.put(TroopTypes.SPEARMEN, 100);
//        map.put(TroopTypes.ARCHERS, 67);
//
//        castleDto.setTypeAndSizeMap(map);
//
//        Castle castle = modelMapper.map(castleDto, Castle.class);
//
//        given(castleService.createArmy(ARMY_SIZE)).willReturn(castle);
//
//
//        mockMvc.perform(get("/api/castle/createarmy/"+ARMY_SIZE)
//                            .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.typeAndSizeMap.size()", is(2)))
//            .andExpect(jsonPath("$.totalSize", is(167)));

    }


}