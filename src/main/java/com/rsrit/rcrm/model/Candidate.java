package com.rsrit.rcrm.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Document(collection = "candidates")
public class Candidate {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String nickName;
    private String emailAddress;
    private String mobileNumber;
    private String workPhoneNumber;
    private String otherPhoneNumber;
    private CustomDate dateOfBirth;
    private String ssn;
    private String skypeId;
    private String linkedinProfileUrl;
    private String facebookProfileUrl;
    private String twitterProfileUrl;
    private String videoReference;

    // Change this to enum or class perhaps
    private String workAuthorization;
    private Boolean clearance;
    private String address;
    private String city;
    // Change these to enum as well
    private String state;
    private String country;
    private String postalCode;
    private String source;

    private Integer experience;
    private String referredBy;
    // enums?
    private String applicantStatus;
    private String applicantGroup;
    private String ownership;
    private String jobTitle;
    private Double expectedPay;
    private String additionalComments;
    private Boolean relocation;
    private List<String> skills;
    private List<String> primarySkills;
    private String technology;
    private String taxTerms;

    // enums?
    private String gender;
    private String raceEthnicity;
    private String veteranStatus;
    private String disability;
    private List<com.rsrit.rcrm.model.Document> documents;

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.toJson(this);
    }

    public Candidate(String firstName, String lastName, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    public Candidate() {

    }

    public Candidate(String id, String firstName, String lastName, String middleName, String nickName, String emailAddress, String mobileNumber, String workPhoneNumber, String otherPhoneNumber,
            CustomDate dateOfBirth, String ssn, String skypeId, String linkedinProfileUrl, String facebookProfileUrl, String twitterProfileUrl, String videoReference, String workAuthorization,
            Boolean clearance, String address, String city, String state, String country, String postalCode, String source, Integer experience, String referredBy, String applicantStatus,
            String applicantGroup, String ownership, String jobTitle, Double expectedPay, String additionalComments, Boolean relocation, List<String> skills, List<String> primarySkills,
            String technology, String taxTerms, String gender, String raceEthnicity, String veteranStatus, String disability, List<com.rsrit.rcrm.model.Document> documents) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.nickName = nickName;
        this.emailAddress = emailAddress;
        this.mobileNumber = mobileNumber;
        this.workPhoneNumber = workPhoneNumber;
        this.otherPhoneNumber = otherPhoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.ssn = ssn;
        this.skypeId = skypeId;
        this.linkedinProfileUrl = linkedinProfileUrl;
        this.facebookProfileUrl = facebookProfileUrl;
        this.twitterProfileUrl = twitterProfileUrl;
        this.videoReference = videoReference;
        this.workAuthorization = workAuthorization;
        this.clearance = clearance;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.source = source;
        this.experience = experience;
        this.referredBy = referredBy;
        this.applicantStatus = applicantStatus;
        this.applicantGroup = applicantGroup;
        this.ownership = ownership;
        this.jobTitle = jobTitle;
        this.expectedPay = expectedPay;
        this.additionalComments = additionalComments;
        this.relocation = relocation;
        this.skills = skills;
        this.primarySkills = primarySkills;
        this.technology = technology;
        this.taxTerms = taxTerms;
        this.gender = gender;
        this.raceEthnicity = raceEthnicity;
        this.veteranStatus = veteranStatus;
        this.disability = disability;
        this.documents = documents;
    }

    public List<com.rsrit.rcrm.model.Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<com.rsrit.rcrm.model.Document> documents) {
        this.documents = documents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getWorkPhoneNumber() {
        return workPhoneNumber;
    }

    public void setWorkPhoneNumber(String workPhoneNumber) {
        this.workPhoneNumber = workPhoneNumber;
    }

    public String getOtherPhoneNumber() {
        return otherPhoneNumber;
    }

    public void setOtherPhoneNumber(String otherPhoneNumber) {
        this.otherPhoneNumber = otherPhoneNumber;
    }

    public CustomDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(CustomDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getSkypeId() {
        return skypeId;
    }

    public void setSkypeId(String skypeId) {
        this.skypeId = skypeId;
    }

    public String getLinkedinProfileUrl() {
        return linkedinProfileUrl;
    }

    public void setLinkedinProfileUrl(String linkedinProfileUrl) {
        this.linkedinProfileUrl = linkedinProfileUrl;
    }

    public String getFacebookProfileUrl() {
        return facebookProfileUrl;
    }

    public void setFacebookProfileUrl(String facebookProfileUrl) {
        this.facebookProfileUrl = facebookProfileUrl;
    }

    public String getTwitterProfileUrl() {
        return twitterProfileUrl;
    }

    public void setTwitterProfileUrl(String twitterProfileUrl) {
        this.twitterProfileUrl = twitterProfileUrl;
    }

    public String getVideoReference() {
        return videoReference;
    }

    public void setVideoReference(String videoReference) {
        this.videoReference = videoReference;
    }

    public String getWorkAuthorization() {
        return workAuthorization;
    }

    public void setWorkAuthorization(String workAuthorization) {
        this.workAuthorization = workAuthorization;
    }

    public Boolean getClearance() {
        return clearance;
    }

    public void setClearance(Boolean clearance) {
        this.clearance = clearance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }

    public String getApplicantStatus() {
        return applicantStatus;
    }

    public void setApplicantStatus(String applicantStatus) {
        this.applicantStatus = applicantStatus;
    }

    public String getApplicantGroup() {
        return applicantGroup;
    }

    public void setApplicantGroup(String applicantGroup) {
        this.applicantGroup = applicantGroup;
    }

    public String getOwnership() {
        return ownership;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Double getExpectedPay() {
        return expectedPay;
    }

    public void setExpectedPay(Double expectedPay) {
        this.expectedPay = expectedPay;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    public Boolean getRelocation() {
        return relocation;
    }

    public void setRelocation(Boolean relocation) {
        this.relocation = relocation;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getPrimarySkills() {
        return primarySkills;
    }

    public void setPrimarySkills(List<String> primarySkills) {
        this.primarySkills = primarySkills;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getTaxTerms() {
        return taxTerms;
    }

    public void setTaxTerms(String taxTerms) {
        this.taxTerms = taxTerms;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRaceEthnicity() {
        return raceEthnicity;
    }

    public void setRaceEthnicity(String raceEthnicity) {
        this.raceEthnicity = raceEthnicity;
    }

    public String getVeteranStatus() {
        return veteranStatus;
    }

    public void setVeteranStatus(String veteranStatus) {
        this.veteranStatus = veteranStatus;
    }

    public String getDisability() {
        return disability;
    }

    public void setDisability(String disability) {
        this.disability = disability;
    }

}
