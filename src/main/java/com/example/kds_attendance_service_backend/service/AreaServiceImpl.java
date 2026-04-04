package com.example.kds_attendance_service_backend.service;

import com.example.kds_attendance_service_backend.dto.CreateAreaRequestDto;
import com.example.kds_attendance_service_backend.dto.DashBoardAreaResponseDto;
import com.example.kds_attendance_service_backend.exception.BadRequestException;
import com.example.kds_attendance_service_backend.exception.ResourceNotFoundException;
import com.example.kds_attendance_service_backend.model.Area;
import com.example.kds_attendance_service_backend.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService{
    private final AreaRepository areaRepository;
    @Override
    public Long createArea(CreateAreaRequestDto createAreaRequestDto) {


   areaRepository.findByName(createAreaRequestDto.getName()).ifPresent(
               a -> {
                   throw new BadRequestException("area " + createAreaRequestDto.getName() + " already present !!");
               });
        String qr = "AREA_"+UUID.randomUUID();

        Area area = Area.builder()
                .name(createAreaRequestDto.getName())
                .longitude(createAreaRequestDto.getLongitude())
                .latitude(createAreaRequestDto.getLatitude())
                .radius(createAreaRequestDto.getRadius())
                .lateTime(createAreaRequestDto.getLateTime())
                .build();


        try {
            Area area1 = areaRepository.save(area);
            areaRepository.flush();
            Long id = area1.getId();
            log.info("area {} created with id {}",area.getName(),id);
            return id;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public List<DashBoardAreaResponseDto> getAreaList() {
        List<Area>areaList = areaRepository.findDistinctAreas();
        if(areaList == null){
            throw  new ResourceNotFoundException("no ara added yet !!");
        }

        List<DashBoardAreaResponseDto> responseDtoList = areaList.stream().map(a->{
           return new DashBoardAreaResponseDto(a.getId(),a.getName());
        }).toList();

        return responseDtoList;

    }
}
