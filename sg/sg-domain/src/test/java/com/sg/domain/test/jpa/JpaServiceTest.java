package com.sg.domain.test.jpa;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sg.constants.Roles;
import com.sg.dto.request.CanvasCreateDto;
import com.sg.dto.request.CanvasDeleteDto;
import com.sg.dto.request.CanvasUpdateDto;
import com.sg.dto.request.ThreadCreateDto;
import com.sg.dto.request.ThreadDeleteDto;
import com.sg.dto.request.ThreadUpdateDto;
import com.sg.domain.service.SgService;
import com.sg.domain.service.exception.SgAccountNotFoundException;
import com.sg.domain.service.exception.SgCanvasAlreadyExistsException;
import com.sg.domain.service.exception.SgCanvasNotFoundException;
import com.sg.domain.service.exception.SgDataValidationException;
import com.sg.domain.service.exception.SgEmailNonVerifiedException;
import com.sg.domain.service.exception.SgInvalidPasswordException;
import com.sg.domain.service.exception.SgSignupAlreadyCompletedException;
import com.sg.domain.service.exception.SgSignupForRegisteredButNonVerifiedEmailException;
import com.sg.domain.service.exception.SgThreadAlreadyExistsException;
import com.sg.domain.service.exception.SgThreadNotFoundException;
import com.sg.domain.spring.configuration.JpaContext;
import com.sg.domain.spring.configuration.JpaServiceContext;
import com.sg.domain.spring.configuration.MapperContext;
import com.sg.domain.spring.configuration.ValidatorContext;
import com.sg.dto.response.AccountDto;
import com.sg.dto.request.CompleteSignupDto;
import com.sg.dto.request.SigninDto;
import com.sg.dto.request.SignupDto;
import com.sg.dto.response.CanvasesListDto;
import com.sg.dto.response.ThreadsListDto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import junit.framework.Assert;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ValidatorContext.class, JpaContext.class, MapperContext.class, JpaServiceContext.class})
public class JpaServiceTest {

    @Autowired
    private SgService service;

    @Autowired
    private DataSource dataSource;

    public JpaServiceTest() {
    }

    private static final String AIDA_14 = "Aida 14";
    private static final String AIDA_18 = "Aida 18";

    private static final String DMC = "DMC";
    private static final String ANCHOR = "Anchor";

    private static final String USER_LAST_NAME = "Tarasova";
    private static final String USER_FIRST_NAME = "Nadezhda";
    private static final LocalDate USER_BIRTH_DATE = LocalDate.parse("1985-01-28");
    private static final String USER_EMAIL = "test@example.com";
    private static final String USER_PASSWORD = "1GoodPassword!";
    private static final String USER_INCORRECT_PASSWORD = "1InvalidPassword!";

    private static final SignupDto signupDto;
    
    private static final SigninDto signinDto;

    private static final CompleteSignupDto completeSignupDto;

    private static final ThreadCreateDto dmcThreadDto;
    private static final ThreadCreateDto anchorThreadDto;
    
    private static final ThreadsListDto.ThreadInfo dmcThreadInfoDto;
    private static final ThreadsListDto.ThreadInfo anchorThreadInfoDto;
    
    private static final CanvasCreateDto aida14CanvasDto;
    private static final CanvasCreateDto aida18CanvasDto;
    
    private static final CanvasesListDto.CanvasInfo aida14CanvasInfoDto;
    private static final CanvasesListDto.CanvasInfo aida18CanvasInfoDto;
    private static final BigDecimal STITCHES_14 = new BigDecimal(14);
    private static final BigDecimal STITCHES_18 = new BigDecimal(18);

    static {
        dmcThreadDto = new ThreadCreateDto();
        dmcThreadDto.setCode(DMC);
        
        dmcThreadInfoDto = new ThreadsListDto.ThreadInfo();
        dmcThreadInfoDto.setCode(DMC);

        anchorThreadDto = new ThreadCreateDto();
        anchorThreadDto.setCode(ANCHOR);
        
        anchorThreadInfoDto = new ThreadsListDto.ThreadInfo();
        anchorThreadInfoDto.setCode(ANCHOR);
        
        aida14CanvasDto = new CanvasCreateDto();
        aida14CanvasDto.setCode(AIDA_14);
        aida14CanvasDto.setStitchesPerInch(STITCHES_14);
        
        aida18CanvasDto = new CanvasCreateDto();
        aida18CanvasDto.setCode(AIDA_18);
        aida18CanvasDto.setStitchesPerInch(STITCHES_18);
        
        aida14CanvasInfoDto = new CanvasesListDto.CanvasInfo();
        aida14CanvasInfoDto.setCode(AIDA_14);
        aida14CanvasInfoDto.setStitchesPerInch(STITCHES_14);
        
        aida18CanvasInfoDto = new CanvasesListDto.CanvasInfo();
        aida18CanvasInfoDto.setCode(AIDA_18);
        aida18CanvasInfoDto.setStitchesPerInch(STITCHES_18);
        

        signupDto = new SignupDto();
        signupDto.setEmail(USER_EMAIL);
        signupDto.setUserBirthDate(USER_BIRTH_DATE);
        signupDto.setUserFirstName(USER_FIRST_NAME);
        signupDto.setUserLastName(USER_LAST_NAME);
        
        signinDto = new SigninDto();
        signinDto.setPassword(USER_PASSWORD);
        signinDto.setEmail(USER_EMAIL);

        completeSignupDto = new CompleteSignupDto();
        completeSignupDto.setPassword(USER_PASSWORD);
    }
    
    @Test
    public void testCanvasCreate() throws SgDataValidationException
    {
        service.create(aida14CanvasDto);
        try {
            service.create(aida14CanvasDto);
            Assert.fail("Expected " + SgCanvasAlreadyExistsException.class.getName());
        } catch (SgCanvasAlreadyExistsException e) {
        }
    }
    
    @Test
    public void testCanvasesList() throws SgDataValidationException {
        service.create(aida14CanvasDto);
        service.create(aida18CanvasDto);
        
        CanvasesListDto canvasesList = new CanvasesListDto();
        List<CanvasesListDto.CanvasInfo> list = new ArrayList<CanvasesListDto.CanvasInfo>();
        list.add(aida14CanvasInfoDto);
        list.add(aida18CanvasInfoDto);
        canvasesList.setCanvases(list);
        
        Assert.assertEquals(canvasesList, service.listCanvases());
    }
    
    @Test
    public void testCanvasUpdate() throws SgDataValidationException {
        service.create(aida14CanvasDto);
        
        CanvasUpdateDto updateDto = new CanvasUpdateDto();
        updateDto.setRefCode(AIDA_18);
        updateDto.setCode(AIDA_14);
        updateDto.setStitchesPerInch(STITCHES_14);

        try {
            service.update(updateDto);
            Assert.fail("Expected " + SgCanvasNotFoundException.class.getName());
        } catch (SgCanvasNotFoundException e) {
        }

        updateDto = new CanvasUpdateDto();
        updateDto.setRefCode(AIDA_14);
        updateDto.setCode(AIDA_18);
        updateDto.setStitchesPerInch(STITCHES_18);
        
        service.update(updateDto);
        
        CanvasesListDto canvasesList = new CanvasesListDto();
        List<CanvasesListDto.CanvasInfo> list = new ArrayList<CanvasesListDto.CanvasInfo>();
        list.add(aida18CanvasInfoDto);
        canvasesList.setCanvases(list);
        
        Assert.assertEquals(canvasesList, service.listCanvases());
        service.create(aida14CanvasDto);

        try {
            service.update(updateDto);
            Assert.fail("Expected " + SgCanvasAlreadyExistsException.class.getName());
        } catch (SgCanvasAlreadyExistsException e) {
        }
    }
    
    @Test
    public void testCanvasDelete() throws SgDataValidationException {
        service.create(aida14CanvasDto);

        CanvasDeleteDto ref = new CanvasDeleteDto();
        ref.setCode(AIDA_18);

        try {
            service.delete(ref);
            Assert.fail("Expected " + SgCanvasNotFoundException.class.getName());
        } catch (SgCanvasNotFoundException e) {
        }
        
        ref = new CanvasDeleteDto();
        ref.setCode(AIDA_14);
        
        service.delete(ref);
        
        Assert.assertEquals(0, service.listCanvases().getCanvases().size());
    }

    @Test
    public void testThreadCreate() throws SgDataValidationException {
        service.create(dmcThreadDto);
        try {
            service.create(dmcThreadDto);
            Assert.fail("Expected " + SgThreadAlreadyExistsException.class.getName());
        } catch (SgThreadAlreadyExistsException e) {
        }
    }

    @Test
    public void testThreadsList() throws SgDataValidationException {
        service.create(dmcThreadDto);
        service.create(anchorThreadDto);
        
        ThreadsListDto threadsList = new ThreadsListDto();
        List<ThreadsListDto.ThreadInfo> list = new ArrayList<ThreadsListDto.ThreadInfo>();
        list.add(dmcThreadInfoDto);
        list.add(anchorThreadInfoDto);
        threadsList.setThreads(list);
        Assert.assertEquals(threadsList, service.listThreads());
    }

    @Test
    public void testThreadUpdate() throws SgDataValidationException {
        service.create(dmcThreadDto);

        ThreadUpdateDto updateDto = new ThreadUpdateDto();
        updateDto.setRefCode(ANCHOR);
        updateDto.setCode(DMC);

        try {
            service.update(updateDto);
            Assert.fail("Expected " + SgThreadNotFoundException.class.getName());
        } catch (SgThreadNotFoundException e) {
        }
        
        updateDto = new ThreadUpdateDto();
        updateDto.setRefCode(DMC);
        updateDto.setCode(ANCHOR);
        service.update(updateDto);
        ThreadsListDto threadsList = new ThreadsListDto();
        List<ThreadsListDto.ThreadInfo> list = new ArrayList<ThreadsListDto.ThreadInfo>();
        list.add(anchorThreadInfoDto);
        threadsList.setThreads(list);
        
        Assert.assertEquals(threadsList, service.listThreads());
        
        service.create(dmcThreadDto);

        try {
            service.update(updateDto);
            Assert.fail("Expected " + SgThreadAlreadyExistsException.class.getName());
        } catch (SgThreadAlreadyExistsException e) {
        }
    }

    @Test
    public void testThreadDelete() throws SgDataValidationException {
        service.create(dmcThreadDto);

        ThreadDeleteDto ref = new ThreadDeleteDto();
        ref.setCode(ANCHOR);

        try {
            service.delete(ref);
            Assert.fail("Expected " + SgThreadNotFoundException.class.getName());
        } catch (SgThreadNotFoundException e) {
        }
        
        ref = new ThreadDeleteDto();
        ref.setCode(DMC);
        
        service.delete(ref);
        
        Assert.assertEquals(0, service.listThreads().getThreads().size());
    }
    
    @Test 
    public void testGetAccountIdByRegistrationEmailThrowsExceptionIfAccountNotFound() throws SgDataValidationException
    {
        try
        {
            service.getAccountIdByRegistrationEmail(signupDto.getEmail());
            Assert.fail("Expected  " + SgAccountNotFoundException.class.getName());
        }
        catch(SgAccountNotFoundException e){
        }
    }
    
    @Test 
    public void testGetAccountInfoThrowsExceptionIfAccountNotFound() throws SgDataValidationException
    {
        try
        {
            service.getAccountInfo(1L);
            Assert.fail("Expected  " + SgAccountNotFoundException.class.getName());
        }
        catch(SgAccountNotFoundException e){
        }
    }
    
    @Test
    public void testSignupAdmin() throws SgDataValidationException {
        service.signupAdmin(signupDto);
        
        Long accountId = service.getAccountIdByRegistrationEmail(signupDto.getEmail());
        
        AccountDto accountDto = service.getAccountInfo(accountId);
        Assert.assertEquals(signupDto.getEmail(), accountDto.getEmail());
        Assert.assertEquals(signupDto.getUserBirthDate(), accountDto.getUserBirthDate());
        Assert.assertEquals(signupDto.getUserFirstName(), accountDto.getUserFirstName());
        Assert.assertEquals(signupDto.getUserLastName(), accountDto.getUserLastName());
        Assert.assertEquals(Boolean.FALSE, accountDto.getEmailVerified());
        
        Assert.assertTrue(accountDto.getRoles().size() == 2);
        Assert.assertTrue(accountDto.getRoles().contains(Roles.ROLE_USER));
        Assert.assertTrue(accountDto.getRoles().contains(Roles.ROLE_ADMIN));
        
        try{
            service.signupUser(signupDto);
            Assert.fail("Expected " + SgSignupForRegisteredButNonVerifiedEmailException.class.getName());
        }
        catch(SgSignupForRegisteredButNonVerifiedEmailException e){
        }
        
        service.completeSignup(accountId, completeSignupDto);
        
        try{
            service.signupUser(signupDto);
            Assert.fail("Expected " + SgSignupAlreadyCompletedException.class.getName());
        }
        catch(SgSignupAlreadyCompletedException e){
        }
    }
    
    @Test
    public void testSignupUser() throws SgDataValidationException {
        service.signupUser(signupDto);
        
        Long accountId = service.getAccountIdByRegistrationEmail(signupDto.getEmail());
        
        AccountDto accountDto = service.getAccountInfo(accountId);
        Assert.assertEquals(signupDto.getEmail(), accountDto.getEmail());
        Assert.assertEquals(signupDto.getUserBirthDate(), accountDto.getUserBirthDate());
        Assert.assertEquals(signupDto.getUserFirstName(), accountDto.getUserFirstName());
        Assert.assertEquals(signupDto.getUserLastName(), accountDto.getUserLastName());
        Assert.assertEquals(Boolean.FALSE, accountDto.getEmailVerified());
        
        Assert.assertTrue(accountDto.getRoles().size() == 1);
        Assert.assertTrue(accountDto.getRoles().contains(Roles.ROLE_USER));
        
        try{
            service.signupUser(signupDto);
            Assert.fail("Expected " + SgSignupForRegisteredButNonVerifiedEmailException.class.getName());
        }
        catch(SgSignupForRegisteredButNonVerifiedEmailException e){
        }
        
        service.completeSignup(accountId, completeSignupDto);
        
        try{
            service.signupUser(signupDto);
            Assert.fail("Expected " + SgSignupAlreadyCompletedException.class.getName());
        }
        catch(SgSignupAlreadyCompletedException e){
        }
    }
    
    @Test
    public void testCompleteSignup() throws SgDataValidationException 
    {
        try{
            service.completeSignup(1L, completeSignupDto);
            Assert.fail("Expected " + SgAccountNotFoundException.class.getName());
        }
        catch(SgAccountNotFoundException e) {
        }
        
        service.signupUser(signupDto);
        
        Long accountId = service.getAccountIdByRegistrationEmail(signupDto.getEmail());
        
        service.completeSignup(accountId, completeSignupDto);
        
        try
        {
            service.completeSignup(accountId, completeSignupDto);
            Assert.fail("Expected " + SgSignupAlreadyCompletedException.class.getName());
        }
        catch(SgSignupAlreadyCompletedException e) {
        }
    }
    
    @Test
    public void testSignin() throws SgDataValidationException
    {
        try{
            service.signIn(signinDto);
            Assert.fail("Expected " + SgAccountNotFoundException.class.getName());
        }
        catch(SgAccountNotFoundException e)
        {}
        
        service.signupUser(signupDto);
        
        try{
            service.signIn(signinDto);
            Assert.fail("Expected " + SgEmailNonVerifiedException.class.getName());
        }
        catch(SgEmailNonVerifiedException e)
        {}
        
        Long accountId = service.getAccountIdByRegistrationEmail(signupDto.getEmail());
        service.completeSignup(accountId, completeSignupDto);
        
        service.signIn(signinDto);
        
        SigninDto invalidSignInDto = new SigninDto();
        invalidSignInDto.setEmail(signinDto.getEmail());
        invalidSignInDto.setPassword(USER_INCORRECT_PASSWORD);
        
        try{
            service.signIn(invalidSignInDto);
            Assert.fail("Expected " + SgInvalidPasswordException.class.getName());
        }
        catch(SgInvalidPasswordException e)
        {}
    }

    @After
    public void cleanupData() throws Exception {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try {
                Statement stmt = connection.createStatement();
                try {
                    stmt.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
                    connection.commit();
                } finally {
                    stmt.close();
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new Exception(e);
            }
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
