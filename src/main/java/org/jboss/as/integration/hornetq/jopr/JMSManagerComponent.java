/*
* JBoss, Home of Professional Open Source
* Copyright 2009, Red Hat Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.as.integration.hornetq.jopr;

import org.jboss.as.integration.hornetq.jopr.util.ManagementSupport;
import org.jboss.deployers.spi.management.ManagementView;
import org.jboss.managed.api.ComponentType;
import org.jboss.managed.api.ManagedOperation;
import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.api.values.SimpleValueSupport;
import org.rhq.core.domain.configuration.Property;
import org.rhq.core.domain.configuration.PropertyList;
import org.rhq.core.domain.configuration.PropertyMap;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.measurement.AvailabilityType;
import org.rhq.core.domain.measurement.MeasurementDataTrait;
import org.rhq.core.domain.measurement.MeasurementReport;
import org.rhq.core.domain.measurement.MeasurementScheduleRequest;
import org.rhq.core.domain.resource.CreateResourceStatus;
import org.rhq.core.pluginapi.inventory.*;
import org.rhq.core.pluginapi.measurement.MeasurementFacet;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author <a href="mailto:andy.taylor@jboss.org">Andy Taylor</a>
 *         Created 11-Mar-2010
 */
public class JMSManagerComponent implements ResourceComponent, CreateChildResourceFacet, MeasurementFacet, JMSComponent
{
   private ResourceContext resourceContext;

   public void getValues(MeasurementReport measurementReport, Set<MeasurementScheduleRequest> measurementScheduleRequests) throws Exception
   {

      for (MeasurementScheduleRequest measurementScheduleRequest : measurementScheduleRequests)
      {

         if("version".equalsIgnoreCase(measurementScheduleRequest.getName()))
         {
            ManagementView managementView = getProfileService();
            ManagedOperation operation = ManagementSupport.getOperation(managementView, "JMSServerMO", "getVersion", new ComponentType("JMSManage", "ServerManage"));
            SimpleValueSupport support = (SimpleValueSupport) operation.invoke();measurementReport.addData(new MeasurementDataTrait(measurementScheduleRequest, support.getValue().toString()));
         }
         else if("started".equalsIgnoreCase(measurementScheduleRequest.getName()))
         {
            ManagementView managementView = getProfileService();
            ManagedOperation operation = ManagementSupport.getOperation(managementView, "JMSServerMO", "isStarted", new ComponentType("JMSManage", "ServerManage"));
            SimpleValueSupport support = (SimpleValueSupport) operation.invoke();measurementReport.addData(new MeasurementDataTrait(measurementScheduleRequest, support.getValue().toString()));
         }
      }

   }

   public CreateResourceReport createResource(CreateResourceReport createResourceReport)
   {
      createResourceReport.setStatus(CreateResourceStatus.IN_PROGRESS);
      ManagementView managementView = null;
      try
      {
         managementView = getProfileService();
      }
      catch (Exception e)
      {
         createResourceReport.setStatus(CreateResourceStatus.FAILURE);
         createResourceReport.setErrorMessage(e.getMessage());
         e.printStackTrace();
         return createResourceReport;
      }
      Map<String, PropertySimple> simpleProps = createResourceReport.getResourceConfiguration().getSimpleProperties();


      try
      {
         if ("JMS Connection Factory".equalsIgnoreCase(createResourceReport.getResourceType().getName()))
         {

            String name = simpleProps.get("name").getStringValue();
            String liveTransportClassNames = simpleProps.get("liveTransportClassNames").getStringValue();
            String liveTransportParams = simpleProps.get("liveTransportParams").getStringValue();
            String backupTransportClassNames = simpleProps.get("backupTransportClassNames").getStringValue();
            String backupTransportParams = simpleProps.get("backupTransportParams").getStringValue();
            String bindings = simpleProps.get("Bindings").getStringValue();
            String discoveryAddress = simpleProps.get("DiscoveryAddress").getStringValue();
            int discoveryPort = simpleProps.get("DiscoveryPort").getIntegerValue();
            long discoveryRefreshTimeout = simpleProps.get("DiscoveryRefreshTimeout").getLongValue();
            String clientId = simpleProps.get("ClientID").getStringValue();
            int dupsOkBatchSize = simpleProps.get("DupsOKBatchSize").getIntegerValue();
            int transactionBatchSize = simpleProps.get("TransactionBatchSize").getIntegerValue();
            long clientFailureCheckPeriod = simpleProps.get("ClientFailureCheckPeriod").getLongValue();
            long connectionTTL = simpleProps.get("ConnectionTTL").getLongValue();
            long callTimeout = simpleProps.get("CallTimeout").getLongValue();
            int consumerWindowSize = simpleProps.get("ConsumerWindowSize").getIntegerValue();
            int confirmationWindowSize = simpleProps.get("ConfirmationWindowSize").getIntegerValue();
            int producerMaxRate = simpleProps.get("ProducerMaxRate").getIntegerValue();
            int producerWindowSize = simpleProps.get("ProducerWindowSize").getIntegerValue();
            boolean cacheLargeMessageClient = simpleProps.get("CacheLargeMessagesClient").getBooleanValue();
            int minLargeMessageSize = simpleProps.get("MinLargeMessageSize").getIntegerValue();
            boolean blockOnNonDurableSend = simpleProps.get("BlockOnNonDurableSend").getBooleanValue();
            boolean blockOnAcknowledge = simpleProps.get("BlockOnAcknowledge").getBooleanValue();
            boolean blockOnDurableSend = simpleProps.get("BlockOnDurableSend").getBooleanValue();
            boolean autoGroup = simpleProps.get("AutoGroup").getBooleanValue();
            boolean preAcknowledge = simpleProps.get("PreAcknowledge").getBooleanValue();
            long maxRetryInterval = simpleProps.get("MaxRetryInterval").getLongValue();
            double retryIntervalMultiplier = simpleProps.get("RetryIntervalMultiplier").getDoubleValue();
            int reconnectAttempts = simpleProps.get("ReconnectAttempts").getIntegerValue();
            boolean failoverOnShutdown = simpleProps.get("FailoverOnServerShutdown").getBooleanValue();
            int scheduledThreadPoolMaxSize = simpleProps.get("ScheduledThreadPoolMaxSize").getIntegerValue();
            int threadPoolMaxSize = simpleProps.get("ThreadPoolMaxSize").getIntegerValue();
            String groupId = simpleProps.get("GroupID").getStringValue();
            int initialMessagePacketSize = simpleProps.get("InitialMessagePacketSize").getIntegerValue();
            boolean useGlobalPools = simpleProps.get("UseGlobalPools").getBooleanValue();
            long retryInterval = simpleProps.get("RetryInterval").getLongValue();
            String connectionLoadBalancingPolicyClassName = simpleProps.get("ConnectionLoadBalancingPolicyClassName").getStringValue();
            createConnectionFactory(createResourceReport, managementView, name,liveTransportClassNames, liveTransportParams, backupTransportClassNames, backupTransportParams, bindings, discoveryAddress, discoveryPort, discoveryRefreshTimeout, clientId, dupsOkBatchSize, transactionBatchSize, clientFailureCheckPeriod, connectionTTL, callTimeout, consumerWindowSize, confirmationWindowSize, producerMaxRate, producerWindowSize, cacheLargeMessageClient, minLargeMessageSize, blockOnNonDurableSend, blockOnAcknowledge, blockOnDurableSend, autoGroup, preAcknowledge, maxRetryInterval, retryIntervalMultiplier, reconnectAttempts, failoverOnShutdown, scheduledThreadPoolMaxSize, threadPoolMaxSize, groupId, initialMessagePacketSize, useGlobalPools, retryInterval, connectionLoadBalancingPolicyClassName);
         }
         else
         {
            String name = simpleProps.get("name").getStringValue();
            String jndiName = simpleProps.get("jndiBindings").getStringValue();
            String DLA = simpleProps.get("dla").getStringValue();
            String expiryAddress = simpleProps.get("expiryAddress").getStringValue();
            int maxSize = simpleProps.get("maxSize").getIntegerValue();
            int pageSize = simpleProps.get("pageSize").getIntegerValue();
            int maxDeliveryAttempts = simpleProps.get("maxDeliveryAttempts").getIntegerValue();
            long redeliveryDelay = simpleProps.get("redeliveryDelay").getLongValue();
            boolean lastValueQueue = simpleProps.get("lastValueQueue").getBooleanValue();
            long redistributionDelay = simpleProps.get("redistributionDelay").getLongValue();
            boolean sendToDLAOnNoRoute = simpleProps.get("sendToDLAOnNoRoute").getBooleanValue();
            String addressFullMessagePolicy = simpleProps.get("addressFullMessagePolicy").getStringValue();
            if ("JMS Queue".equalsIgnoreCase(createResourceReport.getResourceType().getName()))
            {
               createQueue(createResourceReport, managementView, name, jndiName, DLA, expiryAddress, maxSize, pageSize, maxDeliveryAttempts, redeliveryDelay, lastValueQueue, redistributionDelay, sendToDLAOnNoRoute, addressFullMessagePolicy);
            }
            else if ("JMS Topic".equalsIgnoreCase(createResourceReport.getResourceType().getName()))
            {
               createTopic(createResourceReport, managementView, name, jndiName, DLA, expiryAddress, maxSize, pageSize, maxDeliveryAttempts, redeliveryDelay, lastValueQueue, redistributionDelay, sendToDLAOnNoRoute, addressFullMessagePolicy);
            }
         }


      }
      catch (Exception e)
      {
         createResourceReport.setStatus(CreateResourceStatus.FAILURE);
         createResourceReport.setErrorMessage(e.getMessage());
         e.printStackTrace();
      }
      return createResourceReport;
   }

   private void createConnectionFactory(CreateResourceReport createResourceReport, ManagementView managementView, String name,String liveTransportClassNames, String liveTransportParams, String backupTransportClassNames, String backupTransportParams,  String bindings, String discoveryAddress, int discoveryPort, long discoveryRefreshTimeout, String clientId, int dupsOkBatchSize, int transactionBatchSize, long clientFailureCheckPeriod, long connectionTTL, long callTimeout, int consumerWindowSize, int confirmationWindowSize, int producerMaxRate, int producerWindowSize, boolean cacheLargeMessageClient, int minLargeMessageSize, boolean blockOnNonDurableSend, boolean blockOnAcknowledge, boolean blockOnDurableSend, boolean autoGroup, boolean preAcknowledge, long maxRetryInterval, double retryIntervalMultiplier, int reconnectAttempts, boolean failoverOnShutdown, int scheduledThreadPoolMaxSize, int threadPoolMaxSize, String groupId, int initialMessagePacketSize, boolean useGlobalPools, long retryInterval, String connectionLoadBalancingPolicyClassName)
         throws Exception
   {
      ManagedOperation operation = ManagementSupport.getOperation(managementView, JMSConstants.ConnectionFactory.COMPONENT_NAME, "createConnectionFactory", JMSConstants.ConnectionFactory.COMPONENT_TYPE);
      operation.invoke(new SimpleValueSupport(SimpleMetaType.STRING, name),
            new SimpleValueSupport(SimpleMetaType.STRING, liveTransportClassNames),
            new SimpleValueSupport(SimpleMetaType.STRING, liveTransportParams),
            new SimpleValueSupport(SimpleMetaType.STRING, backupTransportClassNames),
            new SimpleValueSupport(SimpleMetaType.STRING, backupTransportParams),
            new SimpleValueSupport(SimpleMetaType.STRING, bindings),
            new SimpleValueSupport(SimpleMetaType.STRING, discoveryAddress),
            new SimpleValueSupport(SimpleMetaType.INTEGER_PRIMITIVE, discoveryPort),
            new SimpleValueSupport(SimpleMetaType.LONG_PRIMITIVE, discoveryRefreshTimeout),
            new SimpleValueSupport(SimpleMetaType.STRING, clientId),
            new SimpleValueSupport(SimpleMetaType.INTEGER_PRIMITIVE, dupsOkBatchSize),
            new SimpleValueSupport(SimpleMetaType.INTEGER_PRIMITIVE, transactionBatchSize),
            new SimpleValueSupport(SimpleMetaType.LONG_PRIMITIVE, clientFailureCheckPeriod),
            new SimpleValueSupport(SimpleMetaType.LONG_PRIMITIVE, connectionTTL),
            new SimpleValueSupport(SimpleMetaType.LONG_PRIMITIVE, callTimeout),
            new SimpleValueSupport(SimpleMetaType.INTEGER_PRIMITIVE, consumerWindowSize),
            new SimpleValueSupport(SimpleMetaType.INTEGER_PRIMITIVE, confirmationWindowSize),
            new SimpleValueSupport(SimpleMetaType.INTEGER_PRIMITIVE, producerMaxRate),
            new SimpleValueSupport(SimpleMetaType.INTEGER_PRIMITIVE, producerWindowSize),
            new SimpleValueSupport(SimpleMetaType.BOOLEAN_PRIMITIVE, cacheLargeMessageClient),
            new SimpleValueSupport(SimpleMetaType.INTEGER_PRIMITIVE, minLargeMessageSize),
            new SimpleValueSupport(SimpleMetaType.BOOLEAN_PRIMITIVE, blockOnNonDurableSend),
            new SimpleValueSupport(SimpleMetaType.BOOLEAN_PRIMITIVE, blockOnAcknowledge),
            new SimpleValueSupport(SimpleMetaType.BOOLEAN_PRIMITIVE, blockOnDurableSend),
            new SimpleValueSupport(SimpleMetaType.BOOLEAN_PRIMITIVE, autoGroup),
            new SimpleValueSupport(SimpleMetaType.BOOLEAN_PRIMITIVE, preAcknowledge),
            new SimpleValueSupport(SimpleMetaType.LONG_PRIMITIVE, maxRetryInterval),
            new SimpleValueSupport(SimpleMetaType.DOUBLE_PRIMITIVE, retryIntervalMultiplier),
            new SimpleValueSupport(SimpleMetaType.INTEGER_PRIMITIVE, reconnectAttempts),
            new SimpleValueSupport(SimpleMetaType.BOOLEAN_PRIMITIVE, failoverOnShutdown),
            new SimpleValueSupport(SimpleMetaType.INTEGER_PRIMITIVE, scheduledThreadPoolMaxSize),
            new SimpleValueSupport(SimpleMetaType.INTEGER_PRIMITIVE, threadPoolMaxSize),
            new SimpleValueSupport(SimpleMetaType.STRING, groupId),
            new SimpleValueSupport(SimpleMetaType.INTEGER_PRIMITIVE, initialMessagePacketSize),
            new SimpleValueSupport(SimpleMetaType.BOOLEAN_PRIMITIVE, useGlobalPools),
            new SimpleValueSupport(SimpleMetaType.LONG_PRIMITIVE, retryInterval),
            new SimpleValueSupport(SimpleMetaType.STRING, connectionLoadBalancingPolicyClassName));

      createResourceReport.setStatus(CreateResourceStatus.SUCCESS);
      createResourceReport.setResourceKey(name);
      createResourceReport.setResourceName(name);
   }

   private void createQueue(CreateResourceReport createResourceReport, ManagementView managementView, String name, String jndiName, String DLA, String expiryAddress, int maxSize, int pageSize, int maxDeliveryAttempts, long redeliveryDelay, boolean lastValueQueue, long redistributionDelay, boolean sendToDLAOnNoRoute, String addressFullMessagePolicy)
         throws Exception
   {
      ManagedOperation operation = ManagementSupport.getOperation(managementView, JMSConstants.Queue.COMPONENT_NAME, "createQueue", JMSConstants.Queue.COMPONENT_TYPE);

      StringBuffer sendRoles = new StringBuffer();
      StringBuffer consumeRoles = new StringBuffer();
      createRoles(createResourceReport, name, sendRoles, consumeRoles);

      operation.invoke(new SimpleValueSupport(SimpleMetaType.STRING, name),
            new SimpleValueSupport(SimpleMetaType.STRING, jndiName),
            new SimpleValueSupport(SimpleMetaType.STRING, DLA),
            new SimpleValueSupport(SimpleMetaType.STRING, expiryAddress),
            new SimpleValueSupport(SimpleMetaType.INTEGER_PRIMITIVE, maxSize),
            new SimpleValueSupport(SimpleMetaType.INTEGER_PRIMITIVE, pageSize),
            new SimpleValueSupport(SimpleMetaType.INTEGER_PRIMITIVE, maxDeliveryAttempts),
            new SimpleValueSupport(SimpleMetaType.LONG_PRIMITIVE, redeliveryDelay),
            new SimpleValueSupport(SimpleMetaType.BOOLEAN_PRIMITIVE, lastValueQueue),
            new SimpleValueSupport(SimpleMetaType.LONG_PRIMITIVE, redistributionDelay),
            new SimpleValueSupport(SimpleMetaType.BOOLEAN_PRIMITIVE, sendToDLAOnNoRoute),
            new SimpleValueSupport(SimpleMetaType.STRING, addressFullMessagePolicy),
            new SimpleValueSupport(SimpleMetaType.STRING, sendRoles.toString()),
            new SimpleValueSupport(SimpleMetaType.STRING, consumeRoles.toString()));
      createResourceReport.setStatus(CreateResourceStatus.SUCCESS);
      createResourceReport.setResourceKey("jms.queue." + name);
      createResourceReport.setResourceName("jms.queue." + name);
   }

   private void createTopic(CreateResourceReport createResourceReport, ManagementView managementView, String name, String jndiName, String DLA, String expiryAddress, int maxSize, int pageSize, int maxDeliveryAttempts, long redeliveryDelay, boolean lastValueQueue, long redistributionDelay, boolean sendToDLAOnNoRoute, String addressFullMessagePolicy)
         throws Exception
   {
      ManagedOperation operation = ManagementSupport.getOperation(managementView, JMSConstants.Topic.COMPONENT_NAME,
            "createTopic", JMSConstants.Topic.COMPONENT_TYPE);

      StringBuffer sendRoles = new StringBuffer();
      StringBuffer consumeRoles = new StringBuffer();
      StringBuffer createNonDurableRoles = new StringBuffer();
      StringBuffer deleteNonDurableRoles = new StringBuffer();
      StringBuffer createDurableRoles = new StringBuffer();
      StringBuffer deleteDurableRoles = new StringBuffer();
      createRoles(createResourceReport, name, sendRoles, consumeRoles, createNonDurableRoles, deleteNonDurableRoles, createDurableRoles, deleteDurableRoles);

      operation.invoke(new SimpleValueSupport(SimpleMetaType.STRING, name),
            new SimpleValueSupport(SimpleMetaType.STRING, jndiName),
            new SimpleValueSupport(SimpleMetaType.STRING, DLA),
            new SimpleValueSupport(SimpleMetaType.STRING, expiryAddress),
            new SimpleValueSupport(SimpleMetaType.INTEGER_PRIMITIVE, maxSize),
            new SimpleValueSupport(SimpleMetaType.INTEGER_PRIMITIVE, pageSize),
            new SimpleValueSupport(SimpleMetaType.INTEGER_PRIMITIVE, maxDeliveryAttempts),
            new SimpleValueSupport(SimpleMetaType.LONG_PRIMITIVE, redeliveryDelay),
            new SimpleValueSupport(SimpleMetaType.BOOLEAN_PRIMITIVE, lastValueQueue),
            new SimpleValueSupport(SimpleMetaType.LONG_PRIMITIVE, redistributionDelay),
            new SimpleValueSupport(SimpleMetaType.BOOLEAN_PRIMITIVE, sendToDLAOnNoRoute),
            new SimpleValueSupport(SimpleMetaType.STRING, addressFullMessagePolicy),
            new SimpleValueSupport(SimpleMetaType.STRING, sendRoles.toString()),
            new SimpleValueSupport(SimpleMetaType.STRING, consumeRoles.toString()),
            new SimpleValueSupport(SimpleMetaType.STRING, createDurableRoles.toString()),
            new SimpleValueSupport(SimpleMetaType.STRING, deleteDurableRoles.toString()),
            new SimpleValueSupport(SimpleMetaType.STRING, createNonDurableRoles.toString()),
            new SimpleValueSupport(SimpleMetaType.STRING, deleteNonDurableRoles.toString()));
      createResourceReport.setStatus(CreateResourceStatus.SUCCESS);
      createResourceReport.setResourceKey("jms.topic." + name);
      createResourceReport.setResourceName("jms.topic." + name);
   }


   public void start(ResourceContext resourceContext) throws InvalidPluginConfigurationException, Exception
   {
      this.resourceContext = resourceContext;
   }

   public void stop()
   {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public AvailabilityType getAvailability()
   {
      return AvailabilityType.UP;
   }

   public ManagementView getProfileService() throws Exception
   {
      ResourceComponent component = resourceContext.getParentResourceComponent();
      Method m = component.getClass().getMethod("getConnection");
      Object conn = m.invoke(component);
      m = conn.getClass().getMethod("getManagementView");
      return (ManagementView) m.invoke(conn);
   }

   private void createRoles(CreateResourceReport configurationUpdateReport, String name, StringBuffer sendRoles, StringBuffer consumeRoles)
   {
      PropertyList propertyList = (PropertyList) configurationUpdateReport.getResourceConfiguration().get("roles");
      List<Property> roles = propertyList.getList();
      for (Property role : roles)
      {
         PropertyMap actRole = (PropertyMap) role;
         boolean send;
         boolean consume;
         PropertySimple simple = (PropertySimple) actRole.get("name");
         name = simple.getStringValue();
         simple = (PropertySimple) actRole.get("send");
         send = simple.getBooleanValue();
         simple = (PropertySimple) actRole.get("consume");
         consume = simple.getBooleanValue();
         if (send)
         {
            if (sendRoles.length() > 0)
            {
               sendRoles.append(",");
            }
            sendRoles.append(name);
         }
         if (consume)
         {
            if (consumeRoles.length() > 0)
            {
               consumeRoles.append(",");
            }
            consumeRoles.append(name);
         }
      }
   }

   private void createRoles(CreateResourceReport configurationUpdateReport, String name, StringBuffer sendRoles, StringBuffer consumeRoles, StringBuffer createNonDurableRoles, StringBuffer deleteNonDurableRoles, StringBuffer createDurableRoles, StringBuffer deleteDurableRoles)
   {
      PropertyList propertyList = (PropertyList) configurationUpdateReport.getResourceConfiguration().get("roles");
      List<Property> roles = propertyList.getList();
      for (Property role : roles)
      {
         PropertyMap actRole = (PropertyMap) role;
         boolean send;
         boolean consume;
         boolean createNonDurableQueue;
         boolean deleteNonDurableQueue;
         boolean createDurableQueue;
         boolean deleteDurableQueue;
         PropertySimple simple = (PropertySimple) actRole.get("name");
         name = simple.getStringValue();
         simple = (PropertySimple) actRole.get("send");
         send = simple.getBooleanValue();
         simple = (PropertySimple) actRole.get("consume");
         consume = simple.getBooleanValue();
         simple = (PropertySimple) actRole.get("createNonDurableQueue");
         createNonDurableQueue = simple.getBooleanValue();
         simple = (PropertySimple) actRole.get("deleteNonDurableQueue");
         deleteNonDurableQueue = simple.getBooleanValue();
         simple = (PropertySimple) actRole.get("createDurableQueue");
         createDurableQueue = simple.getBooleanValue();
         simple = (PropertySimple) actRole.get("deleteDurableQueue");
         deleteDurableQueue = simple.getBooleanValue();
         if (send)
         {
            if (sendRoles.length() > 0)
            {
               sendRoles.append(",");
            }
            sendRoles.append(name);
         }
         if (consume)
         {
            if (consumeRoles.length() > 0)
            {
               consumeRoles.append(",");
            }
            consumeRoles.append(name);
         }
         if (createDurableQueue)
         {
            if (createDurableRoles.length() > 0)
            {
               createDurableRoles.append(",");
            }
            createDurableRoles.append(name);
         }
         if (deleteDurableQueue)
         {
            if (deleteDurableRoles.length() > 0)
            {
               deleteDurableRoles.append(",");
            }
            deleteDurableRoles.append(name);
         }
         if (createNonDurableQueue)
         {
            if (createNonDurableRoles.length() > 0)
            {
               createNonDurableRoles.append(",");
            }
            createNonDurableRoles.append(name);
         }
         if (deleteNonDurableQueue)
         {
            if (deleteNonDurableRoles.length() > 0)
            {
               deleteNonDurableRoles.append(",");
            }
            deleteNonDurableRoles.append(name);
         }
      }
   }
}