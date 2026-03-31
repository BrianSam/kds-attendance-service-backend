package com.example.kds_attendance_service_backend.service;

import com.example.kds_attendance_service_backend.dto.HolidayRequestDto;
import com.example.kds_attendance_service_backend.exception.BadRequestException;
import com.example.kds_attendance_service_backend.model.Area;
import com.example.kds_attendance_service_backend.model.Holiday;
import com.example.kds_attendance_service_backend.repository.AreaRepository;
import com.example.kds_attendance_service_backend.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService
{
    private final HolidayRepository holidayRepository;
    private final AreaRepository areaRepository;
    @Override
    public void createHoliday(HolidayRequestDto requestDto) {
        Area area1 = null;

        if (requestDto.getAreaId() != null) {
            area1 = areaRepository.findById(requestDto.getAreaId())
                    .orElseThrow(() -> new BadRequestException("Area  not found"));
        }

        Holiday holiday = Holiday.builder()
                .date(requestDto.getDate())
                .name(requestDto.getName())
                .area(area1)
                .build();
       if(area1 != null){
           log.info("Holiday Marked for date:{} and area {}",holiday.getDate(),area1.getName());
       }
       else{
           log.info("Holiday Marked for date:{} ",holiday.getDate());
       }

        holidayRepository.save(holiday);
    }
}
