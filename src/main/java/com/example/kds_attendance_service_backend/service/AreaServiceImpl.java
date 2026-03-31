package com.example.kds_attendance_service_backend.service;

import com.example.kds_attendance_service_backend.dto.CreateAreaRequestDto;
import com.example.kds_attendance_service_backend.exception.BadRequestException;
import com.example.kds_attendance_service_backend.model.Area;
import com.example.kds_attendance_service_backend.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
                .qrCode(qr)
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
}
