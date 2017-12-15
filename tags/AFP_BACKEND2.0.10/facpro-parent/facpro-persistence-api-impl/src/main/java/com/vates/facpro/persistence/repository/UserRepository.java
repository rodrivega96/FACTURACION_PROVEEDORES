package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.persistence.domain.UserView;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

	User findByMailIgnoringCase(String mail);

	Long countByMailIgnoringCaseAndIdNot(String mail, Long id);

	User findByMailIgnoringCaseAndPassword(String mail, String password);

	User findByMail(String mail);
	
	Long countById(Long id);

	Long countByIdAndRolesId(Long id, Long rolId);
	
	@Query("SELECT usr.active, usr.id, usr.job, usr.lastName, usr.mail, usr.name, usr.tipo, usr.version, usr.password"
			+ " FROM User as usr where usr.mail=:userMail")
	Object findUserViewByMailIgnoringCase(@Param("userMail") String mail);

	@Query("SELECT usr.active, usr.id, usr.job, usr.lastName, usr.mail, usr.name, usr.tipo, usr.version, usr.password"
			+ " FROM User as usr where usr.mail=:userMail and usr.password=:userPass")
	Object findUserViewByMailIgnoringCaseAndPassword(
			@Param("userMail") String mail, @Param("userPass") String password);
	
	@Query("SELECT DISTINCT mod.largeName, res.representation, rep.representationPage,"
			+ " rep.representation, rep.ord, res.ord, mod.ord"
			+ " from User as usr INNER JOIN usr.roles rl INNER JOIN rl.permissions as per"
			+ " INNER JOIN per.resource as res"
			+ " INNER JOIN per.representations as rep INNER JOIN res.modules mod"
			+ " WHERE usr.id = :userId and"
			+ " rep.representationPage is not null and res.representation is not null")
	List<Object[]> findRolesMenuUser(@Param("userId") Long userId);
	
	@Query("SELECT DISTINCT '', res.representation, rep.representationPage, rep.representation  "
			+ "from User as usr INNER JOIN usr.roles rl INNER JOIN rl.permissions as per INNER JOIN per.resource as res"
			+ " INNER JOIN per.representations as rep WHERE usr.id = :userId ")
	List<Object[]> findUserRoles(@Param("userId") Long userId);
	
	@Query("SELECT usr.mail FROM User as usr where usr.id in (:userIds)")
	List<String> findMailByIdIn(@Param("userIds") List<Long> userIds);
	
	@Query("SELECT usr.lastName || ', ' ||usr.name FROM User as usr where usr.id = :userId)")
	String findNameAndLastNameById(@Param("userId") Long userId);
	
	@Query("SELECT usr.mail FROM User as usr where usr.id=:userId")
	String findMailById(@Param("userId") Long userId);
	
	@Query("SELECT usr.mail FROM User as usr INNER JOIN usr.roles as rl "
			+ "where rl.id=:rolId and usr.active=:active")
	List<String> findEmailsByRolesIdAndActive(@Param("rolId")Long roleId,@Param("active") Integer active);

	Page<UserView> findByNameIsLikeIgnoringCaseAndLastNameIsLikeIgnoringCaseAndActiveAndJobIsLikeAndIdNot(
			String name, String lastName, int active, String job, Long id,
			Pageable pageable);

	Page<UserView> findByNameIsLikeIgnoringCaseAndLastNameIsLikeIgnoringCaseAndJobIsLikeAndIdNot(
			String name, String lastName, String job, Long id, Pageable pageable);

	List<User> findByRolesIdAndActive(Long roleId, int active);	
	
	@Query("SELECT usr.active, usr.id, usr.job, usr.lastName, usr.mail, usr.name, usr.tipo, usr.version"
			+ " FROM User as usr INNER JOIN usr.roles as rl where usr.active=:active and rl.id=:rolId")
	List<Object[]> findBasicByRolesIdAndActive(@Param("rolId") Long rolId, @Param("active") Integer active);
	
	@Query("SELECT usr.active, usr.id, usr.job, usr.lastName, usr.mail, usr.name, usr.tipo, usr.version"
			+ " FROM User as usr where usr.id=:userId")
	Object findSingleBasicById(@Param("userId") Long userId);

	@Query("SELECT usr.active, usr.id, usr.job, usr.lastName, usr.mail, usr.name, usr.tipo, usr.version"
			+ " FROM User as usr WHERE usr.active IN :activeList AND usr.name like :name AND usr.lastName like :lastName AND usr.job like :job AND usr.id NOT IN :idList")
	List<Object[]> findPaginatedList(
			@Param("activeList") List<Integer> activeList,
			@Param("name") String name, @Param("lastName") String lastName,
			@Param("job") String job, @Param("idList") List<Long> idList, Pageable pageable);
	
	@Query("SELECT count(usr)"
			+ " FROM User as usr WHERE usr.active IN :activeList AND usr.name like :name AND usr.lastName like :lastName AND usr.job like :job AND usr.id NOT IN :idList")
	Long countPaginatedList(
			@Param("activeList") List<Integer> activeList,
			@Param("name") String name, @Param("lastName") String lastName,
			@Param("job") String job, @Param("idList") List<Long> idList);

	User findById(Long id);                               
	
}
