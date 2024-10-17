package com.demo.springboot.user;

import com.demo.springboot.address.AddressMapper;
import com.demo.springboot.core.utilities.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(scopeName = "singleton")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDto createUser(final UserDto user) {
        final var userEntity = new UserEntity();
        userEntity.setUserId(0L);
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEntityVersion(1L);
        userEntity.setDateOfBirth(DateTimeFormatter.parseDate(user.getDateOfBirth(), "DD-MM-YYYY"));

        final var addresses = user.getAddresses();

        // if addresses are present...
        if (addresses != null && !addresses.isEmpty()) {
            userEntity.setAddresses(AddressMapper.INSTANCE.fromDto(addresses));

            /*final Set<AddressEntity> addressEntities = new HashSet<>();

            for (final var address : addresses) {
                final var addressEntity = new AddressEntity();
                addressEntity.setAddressId(address.getAddressId());
                addressEntity.setEntityVersion(address.getEntityVersion());
                addressEntity.setCity(address.getCity());
                addressEntity.setCountry(address.getCountry());

                addressEntities.add(addressEntity);
            }

            userEntity.setAddresses(addressEntities);*/
        }

        final var savedUserEntity = repository.saveAndFlush(userEntity);
        final var savedAddressEntities = savedUserEntity.getAddresses();

        user.setUserId(savedUserEntity.getUserId());
        user.setEntityVersion(savedUserEntity.getEntityVersion());

        if (savedAddressEntities != null && !savedAddressEntities.isEmpty()) {
            user.setAddresses(AddressMapper.INSTANCE.toDto(savedAddressEntities));

            /*final Set<AddressDto> _addresses = new HashSet<>();

            for (final var addressEntity : savedAddressEntities) {
                _addresses.add(AddressDto.builder()
                        .addressId(addressEntity.getAddressId())
                        .entityVersion(addressEntity.getEntityVersion())
                        .city(addressEntity.getCity())
                        .country(addressEntity.getCountry())
                        .build());
            }

            user.setAddresses(_addresses);*/
        }

        return user;
    }

    @Override
    public UserDto updateUser(final UserDto user) {
        final var userEntity = new UserEntity();
        userEntity.setUserId(user.getUserId());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEntityVersion(user.getEntityVersion());
        userEntity.setDateOfBirth(DateTimeFormatter.parseDate(user.getDateOfBirth(), "DD-MM-YYYY"));

        final var savedUserEntity = repository.saveAndFlush(userEntity);

        user.setUserId(savedUserEntity.getUserId());
        user.setEntityVersion(savedUserEntity.getEntityVersion());

        return user;
    }
}
