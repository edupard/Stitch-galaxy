/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.service.jpa.components;

import com.sg.rest.dto.validator.components.ValidatorComponent;
import com.sg.constants.Roles;
import com.sg.domain.service.exception.SgServiceLayerRuntimeException;
import com.sg.dto.request.CanvasCreateDto;
import com.sg.dto.request.CanvasDeleteDto;
import com.sg.dto.request.CanvasUpdateDto;
import com.sg.dto.request.ThreadCreateDto;
import com.sg.dto.request.ThreadDeleteDto;
import com.sg.dto.request.ThreadUpdateDto;
import com.sg.domain.entities.jpa.Canvas;
import com.sg.domain.repositories.CanvasesRepository;
import com.sg.domain.repositories.ProductRepository;
import com.sg.domain.repositories.ThreadsRepository;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.transaction.support.TransactionTemplate;
import com.sg.domain.entities.jpa.Thread;
import com.sg.domain.entities.jpa.Account;
import com.sg.domain.repositories.AccountsRepository;
import com.sg.domain.service.SgService;
import com.sg.domain.service.exception.SgAccountNotFoundException;
import com.sg.domain.service.exception.SgAccountWithoutEmailException;
import com.sg.domain.service.exception.SgCanvasAlreadyExistsException;
import com.sg.domain.service.exception.SgCanvasNotFoundException;
import com.sg.domain.service.exception.SgDataValidationException;
import com.sg.domain.service.exception.SgEmailNonVerifiedException;
import com.sg.domain.service.exception.SgInstallationAlreadyCompletedException;
import com.sg.domain.service.exception.SgInvalidPasswordException;
import com.sg.domain.service.exception.SgSignupAlreadyCompletedException;
import com.sg.domain.service.exception.SgSignupForRegisteredButNonVerifiedEmailException;
import com.sg.domain.service.exception.SgThreadAlreadyExistsException;
import com.sg.domain.service.exception.SgThreadNotFoundException;
import com.sg.dto.request.CompleteSignupDto;
import com.sg.dto.request.ResetPasswordDto;
import com.sg.dto.request.SigninDto;
import com.sg.dto.request.SignupDto;
import com.sg.dto.request.UserInfoUpdateDto;
import com.sg.dto.response.AccountRolesDto;
import com.sg.dto.response.CanvasesListDto;
import com.sg.dto.response.ThreadsListDto;
import com.sg.dto.response.UserInfoDto;
import java.util.ArrayList;
import java.util.Arrays;
import javax.validation.Valid;
import ma.glasnost.orika.MapperFacade;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.validation.annotation.Validated;

/**
 *
 * @author tarasev
 */
//@Transactional annotation prevent spring context to be initialized on GAE
@Service
@Validated
public class ServiceImpl implements SgService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ThreadsRepository threadsRepository;

    @Autowired
    private CanvasesRepository canvasesRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private MapperFacade mapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private ValidatorComponent validatorComponent;

    @Value("${admin.email}")
    private String ADMIN_EMAIL;
    @Value("${admin.password}")
    private String ADMIN_PASSWORD;
    @Value("${user.email}")
    private String USER_EMAIL;
    @Value("${user.password}")
    private String USER_PASSWORD;

    @Override
    public String convertToUpperCase(String input) {

        if ("returnnull".equalsIgnoreCase(input)) {
            return null;
        }

        return input.toUpperCase();
    }

    @Override
    public void delete(@Valid final ThreadDeleteDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    deleteThreadImpl(dto);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private void deleteThreadImpl(ThreadDeleteDto dto) {
        Thread thread = threadsRepository.findByCode(dto.getCode());
        if (thread == null) {
            throw new SgThreadNotFoundException(dto.getCode());
        }
        threadsRepository.delete(thread.getId());
    }

    public void create(final ThreadCreateDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    createThreadImpl(dto);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private void createThreadImpl(ThreadCreateDto dto) {
        if (threadsRepository.findByCode(dto.getCode()) != null) {
            throw new SgThreadAlreadyExistsException(dto.getCode());
        }

        Thread thread = mapper.map(dto, Thread.class);
        threadsRepository.save(thread);
    }

    public ThreadsListDto listThreads() {
        return transactionTemplate.execute(new TransactionCallback<ThreadsListDto>() {
            public ThreadsListDto doInTransaction(TransactionStatus status) {
                try {
                    return listThreadsImpl();
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private ThreadsListDto listThreadsImpl() {
        ThreadsListDto result = new ThreadsListDto();
        Iterable<Thread> threads = threadsRepository.findAll();

        List<ThreadsListDto.ThreadInfo> list = new ArrayList<ThreadsListDto.ThreadInfo>();
        for (Thread t : threads) {
            list.add(mapper.map(t, ThreadsListDto.ThreadInfo.class));
        }

        result.setThreads(list);
        return result;
    }

    public void update(final ThreadUpdateDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    updateThreadImpl(dto);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private void updateThreadImpl(ThreadUpdateDto dto) {
        Thread thread = threadsRepository.findByCode(dto.getRefCode());
        if (thread == null) {
            throw new SgThreadNotFoundException(dto.getRefCode());
        }
        if (threadsRepository.findByCode(dto.getCode()) != null) {
            throw new SgThreadAlreadyExistsException(dto.getCode());
        }

        mapper.map(dto, thread);
        threadsRepository.save(thread);
    }

    public void create(final CanvasCreateDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    createCanvasImpl(dto);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private void createCanvasImpl(CanvasCreateDto dto) {
        if (canvasesRepository.findByCode(dto.getCode()) != null) {
            throw new SgCanvasAlreadyExistsException(dto.getCode());
        }
        Canvas canvas = mapper.map(dto, Canvas.class);
        canvasesRepository.save(canvas);
    }

    public void delete(final CanvasDeleteDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    deleteCanvasImpl(dto);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private void deleteCanvasImpl(CanvasDeleteDto dto) {
        Canvas canvas = canvasesRepository.findByCode(dto.getCode());
        if (canvas == null) {
            throw new SgCanvasNotFoundException(dto.getCode());
        }
        canvasesRepository.delete(canvas);
    }

    public void update(final CanvasUpdateDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    updateCanvasImpl(dto);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private void updateCanvasImpl(CanvasUpdateDto dto) {
        Canvas canvas = canvasesRepository.findByCode(dto.getRefCode());
        if (canvas == null) {
            throw new SgCanvasNotFoundException(dto.getRefCode());
        }
        if (canvasesRepository.findByCode(dto.getCode()) != null) {
            throw new SgCanvasAlreadyExistsException(dto.getCode());
        }
        mapper.map(dto, canvas);
        canvasesRepository.save(canvas);
    }

    public CanvasesListDto listCanvases() {
        return transactionTemplate.execute(new TransactionCallback<CanvasesListDto>() {
            public CanvasesListDto doInTransaction(TransactionStatus status) {
                try {
                    return listCanvasesImpl();
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private CanvasesListDto listCanvasesImpl() {
        CanvasesListDto result = new CanvasesListDto();
        Iterable<Canvas> canvases = canvasesRepository.findAll();

        List<CanvasesListDto.CanvasInfo> list = new ArrayList<CanvasesListDto.CanvasInfo>();
        for (Canvas c : canvases) {
            list.add(mapper.map(c, CanvasesListDto.CanvasInfo.class));
        }

        result.setCanvases(list);
        return result;
    }

    public void signupUser(final SignupDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        signup(dto, Roles.ROLE_USER);
    }

    public void signupAdmin(final SignupDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        signup(dto, Roles.ROLE_ADMIN, Roles.ROLE_USER);
    }

    private void signup(final SignupDto dto, final String... roles) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    signupImpl(dto, roles);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    public void signupImpl(SignupDto dto, String... roles) {
        Account account = accountsRepository.findByEmail(dto.getEmail());
        if (account != null) {
            if (account.getEmailVerified() == Boolean.FALSE) {
                throw new SgSignupForRegisteredButNonVerifiedEmailException(dto.getEmail());
            } else {
                throw new SgSignupAlreadyCompletedException(dto.getEmail());
            }
        }
        account = mapper.map(dto, Account.class);
        account.setEmailVerified(Boolean.FALSE);
        account.setRoles(Arrays.asList(roles));
        accountsRepository.save(account);
    }

    public void completeSignup(final Long userId, final CompleteSignupDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    completeSignupImpl(userId, dto);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    public void completeSignupImpl(Long accountId, CompleteSignupDto dto) {
        Account account = accountsRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        if (account.getEmailVerified() == Boolean.TRUE) {
            throw new SgSignupAlreadyCompletedException(account.getEmail());
        }
        account.setEmailVerified(Boolean.TRUE);
        account.setPassword(dto.getPassword());
        accountsRepository.save(account);
    }

    public void signIn(final SigninDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    signInImpl(dto);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    public void signInImpl(SigninDto dto) {
        Account account = accountsRepository.findByEmail(dto.getEmail());
        if (account == null) {
            throw new SgAccountNotFoundException(dto.getEmail());
        }
        if (account.getEmailVerified() == Boolean.FALSE) {
            throw new SgEmailNonVerifiedException(dto.getEmail());
        }
        if (!dto.getPassword().equals(account.getPassword())) {
            throw new SgInvalidPasswordException();
        }
    }

    public void ping() {
    }

    public void install() throws SgInstallationAlreadyCompletedException {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    installImpl();
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private static final LocalDate BIRTH_DATE = LocalDate.parse("1984-07-10");

    private void installImpl() {
        Account account = accountsRepository.findByEmail(ADMIN_EMAIL);
        if (account != null) {
            throw new SgInstallationAlreadyCompletedException();
        }
        account = new Account();
        account.setEmail(ADMIN_EMAIL);
        account.setEmailVerified(Boolean.TRUE);
        account.setPassword(ADMIN_PASSWORD);
        account.setUserFirstName("admin");
        account.setUserLastName("admin");
        account.setUserBirthDate(BIRTH_DATE);
        account.setRoles(Arrays.asList(Roles.ROLE_ADMIN, Roles.ROLE_USER));
        accountsRepository.save(account);

        account = new Account();
        account.setEmail(USER_EMAIL);
        account.setEmailVerified(Boolean.TRUE);
        account.setPassword(USER_PASSWORD);
        account.setUserFirstName("user");
        account.setUserLastName("user");
        account.setUserBirthDate(BIRTH_DATE);
        account.setRoles(Arrays.asList(Roles.ROLE_USER));
        accountsRepository.save(account);
    }

    public void setUserInfo(final Long accountId, final UserInfoUpdateDto dto) throws SgDataValidationException, SgAccountNotFoundException {
        validatorComponent.validate(dto);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    setUserInfoImpl(accountId, dto);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private void setUserInfoImpl(Long accountId, UserInfoUpdateDto dto) throws SgDataValidationException, SgAccountNotFoundException {
        Account account = accountsRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        mapper.map(dto, account);
        accountsRepository.save(account);
    }

    public UserInfoDto getUserInfo(final Long accountId) throws SgAccountNotFoundException {
        return transactionTemplate.execute(new TransactionCallback<UserInfoDto>() {
            public UserInfoDto doInTransaction(TransactionStatus status) {
                try {
                    return getUserInfoImpl(accountId);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private UserInfoDto getUserInfoImpl(final Long accountId) throws SgAccountNotFoundException {
        Account account = accountsRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        return mapper.map(account, UserInfoDto.class);
    }

    public void deleteAccount(final Long accountId) throws SgAccountNotFoundException {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    deleteAccountImpl(accountId);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private void deleteAccountImpl(Long accountId) throws SgAccountNotFoundException {
        Account account = accountsRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        accountsRepository.delete(account);
    }

    public void resetPassword(final Long accountId, final ResetPasswordDto dto) throws SgDataValidationException, SgAccountNotFoundException {
        validatorComponent.validate(dto);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    resetPasswordImpl(accountId, dto);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private void resetPasswordImpl(Long accountId, ResetPasswordDto dto) throws SgDataValidationException, SgAccountNotFoundException, SgAccountWithoutEmailException {
        Account account = accountsRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        if (account.getEmail() == null) {
            throw new SgAccountWithoutEmailException(accountId);
        }
        if (account.getEmailVerified() == Boolean.FALSE) {
            throw new SgEmailNonVerifiedException((account.getEmail()));
        }
        mapper.map(dto, account);
        accountsRepository.save(account);
    }

    @Override
    public Long getAccountId(final String email) throws SgDataValidationException, SgAccountNotFoundException {
        return transactionTemplate.execute(new TransactionCallback<Long>() {
            public Long doInTransaction(TransactionStatus status) {
                try {
                    return getAccountIdImpl(email);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private Long getAccountIdImpl(String email) {
        Account account = accountsRepository.findByEmail(email);
        if (account == null) {
            throw new SgAccountNotFoundException(email);
        }
        return account.getId();
    }

    @Override
    public AccountRolesDto getAccountRoles(final Long accountId) throws SgAccountNotFoundException {
        return transactionTemplate.execute(new TransactionCallback<AccountRolesDto>() {
            public AccountRolesDto doInTransaction(TransactionStatus status) {
                try {
                    return getAccountRolesImpl(accountId);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    public AccountRolesDto getAccountRolesImpl(Long accountId) throws SgAccountNotFoundException {
        Account account = accountsRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        return mapper.map(account, AccountRolesDto.class);
    }
}
