package com.example.eumserver.domain.resume;

import com.example.eumserver.domain.resume.dto.ResumeRequest;
import com.example.eumserver.domain.resume.dto.ResumeResponse;
import com.example.eumserver.domain.resume.entity.Resume;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ResumeMapper {
    ResumeMapper INSTANCE = Mappers.getMapper(ResumeMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timeStamp", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "careers", source = "careers")
    @Mapping(target = "activities", source = "activities")
    @Mapping(target = "certificates", source = "certificates")
    @Mapping(target = "projects", source = "projects")
    @Mapping(target = "homepages", source = "homepages")
    Resume resumeRequestToResume(ResumeRequest resumeRequest);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "timeStamp.createDate", target = "createDate")
    @Mapping(source = "timeStamp.updateAt", target = "updateAt")
    ResumeResponse resumeToResumeResponse(Resume resume);

    @AfterMapping
    default void linkResume(@MappingTarget Resume resume) {
        if (resume.getActivities() != null) {
            resume.getActivities().forEach(resumeActivity -> resumeActivity.setResume(resume));
        }
        if (resume.getCareers() != null) {
            resume.getCareers().forEach(resumeCareer -> resumeCareer.setResume(resume));
        }
        if (resume.getCertificates() != null) {
            resume.getCertificates().forEach(resumeCertificate -> resumeCertificate.setResume(resume));
        }
        if (resume.getHomepages() != null) {
            resume.getHomepages().forEach(resumeHomepage -> resumeHomepage.setResume(resume));
        }
        if (resume.getProjects() != null) {
            resume.getProjects().forEach(resumeProject -> resumeProject.setResume(resume));
        }
    }
}
