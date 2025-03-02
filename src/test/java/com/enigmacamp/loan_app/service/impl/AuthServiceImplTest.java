package com.enigmacamp.loan_app.service.impl;

import com.enigmacamp.loan_app.constant.ERole;
import com.enigmacamp.loan_app.entity.AppUser;
import com.enigmacamp.loan_app.entity.Customer;
import com.enigmacamp.loan_app.entity.Role;
import com.enigmacamp.loan_app.model.request.AuthRequest;
import com.enigmacamp.loan_app.model.response.AuthResponse;
import com.enigmacamp.loan_app.model.response.SignupResponse;
import com.enigmacamp.loan_app.repository.AppUserRepository;
import com.enigmacamp.loan_app.repository.RoleRepository;
import com.enigmacamp.loan_app.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    private AppUser user;
    private AuthRequest authRequest;
    private Customer customer;
    private Role role;
    private ERole mockRole;

    @BeforeEach
    void setUp() {
        mockRole = Mockito.mock(ERole.class);

        role = Role.builder()
                .role(mockRole)
                .build();

        user = AppUser.builder()
                .id("1")
                .email("test@test.com")
                .password("test")
                .roles(List.of(role))
                .build();

        customer = Customer.builder()
                .user(user)
                .build();

        user.setCustomer(customer);

        authRequest = AuthRequest.builder()
                .email("test@test.com")
                .password("test")
                .build();
    }

    @Test
    void shouldCreateUserSuccessfully() {
        when(roleRepository.findByRole(any(ERole.class))).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(appUserRepository.saveAndFlush(any(AppUser.class))).thenReturn(user);
        when(role.getRole().getDescription()).thenReturn("ROLE_TEST");

        SignupResponse createUser = authService.createUser(authRequest);

        assertNotNull(createUser);

        assertEquals("test@test.com", createUser.getEmail());
        assertEquals(List.of("ROLE_TEST"), createUser.getRole());

        verify(passwordEncoder, times(1)).encode(any(String.class));
        verify(appUserRepository, times(1)).saveAndFlush(any(AppUser.class));
    }

    @Test
    void shouldReturnsAdminAndStaffRolesForAdminEmail() {
        Role adminRole = Role.builder()
                .role(ERole.ROLE_ADMIN)
                .build();

        Role staffRole = Role.builder()
                .role(ERole.ROLE_STAFF)
                .build();

        AuthRequest admin = AuthRequest.builder()
                .email("admin@admin.com")
                .password("admin")
                .build();

        when(roleRepository.findByRole(ERole.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));
        when(roleRepository.findByRole(ERole.ROLE_STAFF)).thenReturn(Optional.of(staffRole));

        SignupResponse createUser = authService.createUser(admin);

        assertNotNull(createUser);

        assertEquals(2, createUser.getRole().size());
        assertEquals("admin@admin.com", createUser.getEmail());
        assertThat(createUser.getRole()).containsExactlyInAnyOrder(adminRole.getRole().getDescription(), staffRole.getRole().getDescription());
    }

    @Test
    void shouldReturnsStaffRoleForStaffEmail() {
        Role staffRole = Role.builder()
                .role(ERole.ROLE_STAFF)
                .build();

        AuthRequest staff = AuthRequest.builder()
                .email("staff@staff.com")
                .password("staff")
                .build();

        when(roleRepository.findByRole(ERole.ROLE_STAFF)).thenReturn(Optional.of(staffRole));

        SignupResponse createUser = authService.createUser(staff);

        assertNotNull(createUser);

        assertEquals(1, createUser.getRole().size());
        assertEquals("staff@staff.com", createUser.getEmail());
        assertThat(createUser.getRole()).containsExactlyInAnyOrder(staffRole.getRole().getDescription());

    }

    @Test
    void shouldReturnsCustomerRoleForCustomerEmail() {
        Role customerRole = Role.builder()
                .role(ERole.ROLE_STAFF)
                .build();

        AuthRequest customer = AuthRequest.builder()
                .email("customer@gmail.com")
                .password("customer")
                .build();

        when(roleRepository.findByRole(ERole.ROLE_CUSTOMER)).thenReturn(Optional.of(customerRole));

        SignupResponse createUser = authService.createUser(customer);

        assertNotNull(createUser);

        assertEquals(1, createUser.getRole().size());
        assertEquals("customer@gmail.com", createUser.getEmail());
        assertThat(createUser.getRole()).containsExactlyInAnyOrder(customerRole.getRole().getDescription());
    }

    @Test
    void shouldThrowExceptionWhenEmailExists() {
        AuthRequest request = new AuthRequest();
        request.setEmail("existing@test.com");
        request.setPassword("test");

        when(roleRepository.findByRole(any(ERole.class))).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(appUserRepository.saveAndFlush(any(AppUser.class))).thenThrow(DataIntegrityViolationException.class);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authService.createUser(request)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Email Duplicate", exception.getReason());
    }

    @Test
    void shouldLoginUserSuccessfully() {
        Authentication mockAuthentication = mock(Authentication.class);
        UserDetails mockUserDetails = mock(UserDetails.class);
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(ERole.ROLE_ADMIN.name());
        Collection<SimpleGrantedAuthority> authorities = List.of(simpleGrantedAuthority);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        doReturn(authorities).when(mockUserDetails).getAuthorities();
        when(mockUserDetails.getUsername()).thenReturn("test@test.com");
        when(jwtTokenProvider.generateToken(anyString(), anyList())).thenReturn("token");

        AuthResponse userLogin = authService.login(authRequest);

        assertNotNull(userLogin);

        assertEquals("test@test.com", userLogin.getEmail());
        assertEquals(List.of("admin"), userLogin.getRole());
        assertEquals("token", userLogin.getToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, times(1)).generateToken(anyString(), anyList());
    }

    @Test
    void shouldThrowExceptionWhenCredentialsInvalid() {
        AuthRequest badCredentials = AuthRequest.builder()
                .email("random@random.com")
                .password("random")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Credentials"));

        ResponseStatusException exception = assertThrowsExactly(ResponseStatusException.class, () -> authService.login(badCredentials));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, never()).generateToken(anyString(), anyList());
    }
}
