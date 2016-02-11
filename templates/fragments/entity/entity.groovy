package $pack

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Column
import javax.persistence.SequenceGenerator
import javax.persistence.Table
	 
@Entity  @Table(name="$table")
class $clazz {
	 
   @Id @SequenceGenerator(name = "gen", sequenceName = "SEQ_MSSAMPLES_ID")
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "gen")
	
<% properties.each{
   if (it.field.toUpperCase() != it.column) print "   @Column(name=\"${it.column}\")" %>
   $it.type $it.field
<%}%> 


}